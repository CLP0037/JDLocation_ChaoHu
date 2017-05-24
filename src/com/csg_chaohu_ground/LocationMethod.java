package com.csg_chaohu_ground;

import java.util.*;

public class LocationMethod {
	
	final int PERMFAULT = 1;                        //�����Թ���
	final int TEMPFAULT = 0;                        //˲ʱ�Թ���
	final int FAULT_PHASE = 1;                      //��SOE�ӵع�������
	final double SIG_PHASE_CURUENT = 1.0;             //����ӵ�SOE���ɵ�����ֵ   
	String firstFaultPoint="", firstNOFaultPoint="";//�����һ�����ϵ�͵�һ���ǹ��ϵ�  
	String  headTermID = null;                      //��������ͼ�ĵ�һ���豸�ն�ID
	
	//������������ȥ�����ն˵����ݣ������豸�ն�map
	public Map<String, TerminalDataJD> getTermMap(Map<String, TerminalDataJD> termMap, String headTerm){
		Map<String, TerminalDataJD> map_re = new HashMap<String, TerminalDataJD>();//�����ն�Map		
		Stack<TerminalDataJD> termStack = new Stack<TerminalDataJD>();
		TerminalDataJD TerminalDataJD = new TerminalDataJD();
		boolean headTermFlag=true, hasBranch=false;
		
		termStack.push(termMap.get(headTerm));
		
		while(!termStack.isEmpty()){
			TerminalDataJD = termStack.pop();
			
			if(TerminalDataJD == null){
				continue;
			}
			//��ǰ�ڵ����豸�ն�����������ȥ�������Ժ��ӽ��б���
			if(TerminalDataJD.getDiviceId() != "-1"){
				System.out.println("�����նˣ�"+TerminalDataJD.getDiviceId());
				System.out.println(TerminalDataJD.toString());//��ӡ�ն���Ϣ
				if(map_re.isEmpty()){
					headTermID = TerminalDataJD.getDiviceId();
				}
				map_re.put(TerminalDataJD.getDiviceId(), TerminalDataJD);
			}else{
				//ɾ�����ĺ󲿽��ָ��ǰ�����     ��ȥ���ڵ��ǰһ���ն�ID����ֵ��ȥ���ڵ�ĺ�һ���ڵ��beforeTerm��������·��ϵ
				if(TerminalDataJD.getAfterTerm() != null){//��ǰ�ն��Ƿ��к��ӽڵ�
					String[] afterTermStr = TerminalDataJD.getAfterTerm().split(",");//�ָ���ն˵��ַ���������
					if(afterTermStr.length > 1){//�з�֧��־����ΪѰ��ͷ���ʹ��
						hasBranch = true;
					}
					for(int i=0; i<afterTermStr.length; i++){
						TerminalDataJD afterNode = termMap.get(afterTermStr[i]);
						afterNode.setBeforeTerm(TerminalDataJD.getBeforeTerm());
					}
				}
				//ɾ������ǰ�����ָ��󲿽��
				if(TerminalDataJD.getBeforeTerm() != null){//ǰ��Ӧ��ֻ��һ�����
					map_re.get(TerminalDataJD.getBeforeTerm()).setAfterTerm(TerminalDataJD.getAfterTerm());
					System.out.println(map_re.get(TerminalDataJD.getBeforeTerm()).toString());//��ӡ�ն���Ϣ
				}
			}
			
			//Ѱ�����˵�ͷ���(�������豸�ն�Ҳ������������)
			if(headTermFlag && hasBranch && map_re.isEmpty()){
				headTermFlag = false;
				hasBranch = false;
				headTermID = TerminalDataJD.getDiviceId();//ͷ���ID
				map_re.put(TerminalDataJD.getDiviceId(), TerminalDataJD);
			}
			
			//�Ե�ǰ�ն˵ĺ��ӽ��б���
			if(TerminalDataJD.getAfterTerm() != null){//��ǰ�ն��к��ӽڵ�
				String[] afterTermStr = TerminalDataJD.getAfterTerm().split(",");//�ָ���ն˵��ַ���������
				
				for(int i=0; i<afterTermStr.length; i++){
					termStack.push(termMap.get(afterTermStr[i]));//��ǰ�ն˵ĺ��ӽڵ���ջ
				}
			}
		}
		
		return map_re;
	}	
	
	//�ն˽ӵ�SOE����ͳ��
	int getFSOECount(Map<String, TerminalDataJD> termMap, String termID){
		int count_re = -1;
		
		if(!termMap.containsKey(termID)){
			return count_re;
		}
		
		TerminalDataJD TerminalDataJD = termMap.get(termID);
		count_re = TerminalDataJD.getJDSOE_A() + TerminalDataJD.getJDSOE_B() + TerminalDataJD.getJDSOE_C();
		
		return count_re;
	}
	
	//�����ж��߼�  ========ˢ�µ���ǰ�豸Ϊֹ��ǰ��Ĺ����豸���ͷǹ����豸�����Լ��ӳ�վ�㵽���һ�������豸ID�ͷǹ����豸ID
	void faultJudge(TerminalDataJD TerminalDataJD, Map<String, TerminalDataJD> termMap){
		 
		int faultCountTemp, noFaultCountTemp;                                  //���Ϻͷǹ����ն�ͳ�Ƶ���ʱ����
		//ͳ�ƽӵ�SOE����
		int JDSOECount = TerminalDataJD.getJDSOE_A() + TerminalDataJD.getJDSOE_B() + TerminalDataJD.getJDSOE_C();
		
		if(TerminalDataJD.getBeforeTerm() != null){//ȡǰһ���ն˵Ĺ�����ͳ��
			if(termMap.containsKey(TerminalDataJD.getBeforeTerm())){
				faultCountTemp = termMap.get(TerminalDataJD.getBeforeTerm()).getFaultCount();
				noFaultCountTemp = termMap.get(TerminalDataJD.getBeforeTerm()).getNoFaultCount();
			}else{
				faultCountTemp = 0;
				noFaultCountTemp = 0;
			}
		}else{//ǰһ���ն�Ϊ����ͷ��㣬�����ն�ͳ��Ϊ��
			faultCountTemp = 0;
			noFaultCountTemp = 0;
		}
	
		if(JDSOECount >= FAULT_PHASE){//��SOE���ն�
			firstFaultPoint = TerminalDataJD.getDiviceId();                      //���汾��·�ĵ�һ�����ϵ㣻
			firstNOFaultPoint = "";//null                                      //���֮ǰ����ķǹ��ϵ㣬��һ���ǹ��ϵ�Ӧ�ڱ����ϵ�֮��		
			//�����ն�ͳ��
			faultCountTemp += 1;			
		}else if(JDSOECount==0){//û�б�SOE���ն�
			if(firstNOFaultPoint==""){//null                                   //***LineFlag
				firstNOFaultPoint = TerminalDataJD.getDiviceId();                // ���汾��·�ĵ�һ���ǹ��ϵ㣻
			}		
			//�ǹ����ն�ͳ�� ���ն˺󲿽��ֻҪ��һ���Ǳ�SOE���ն˾ͼ���
			if (TerminalDataJD.getAfterTerm() != null) {                         // ��ǰ�ն��Ƿ��к��ӽڵ�
				String[] afterTermStr = TerminalDataJD.getAfterTerm().split(",");// �ָ���ն˵��ַ���������
				for(int i=0; i<afterTermStr.length; i++){
					TerminalDataJD afterNode = termMap.get(afterTermStr[i]);
					if((afterNode.getJDSOE_A()+afterNode.getJDSOE_B()+afterNode.getJDSOE_C())>=FAULT_PHASE){//������������δ�����ϵ��ն�
						noFaultCountTemp += 1;//�ǹ����ն�ͳ��
						break;
					}
				}
			}
		}
		
		//�������ն�ͳ�Ƽ������浽���ն˶���(TerminalDataJD)
		TerminalDataJD.setFaultCount(faultCountTemp);
		TerminalDataJD.setNoFaultCount(noFaultCountTemp);
	}
	
	/**
	 * ���ӵ�SOE©���о�
	 * @param TerminalDataJD
	 * @param faultRat �ն��ϱ�����ֵ
	 * @param JDType �ӵ����� (0 ��Դ�ӵ�   1 ��Դ�ӵ�)
	 * @return true  �����ϱ��ʴ��ڵ�����ֵ  ����false 
	 * 
	 */
	boolean faultMissingJudge(TerminalDataJD TerminalDataJD, double faultRat,int JDType){
		boolean re_val;	
//		double f = (double)TerminalDataJD.getFaultCount()/(TerminalDataJD.getFaultCount()+TerminalDataJD.getNoFaultCount());
//
//		if(f >= faultRat){
//	    	re_val = true;
//	    }else {
//			re_val = false;
//		}
	    

		if(TerminalDataJD.getFaultCount()>=2)//���ϱ����ϵ����һ���豸===�������豸����
		{
			re_val = true;
		}
		else
		{
			if((TerminalDataJD.getFaultCount()+TerminalDataJD.getNoFaultCount())==1)//���ϱ����ϵ����һ���豸=====�豸����
			{
				re_val = true;
			}
			else
				re_val = false;
		}
		
	    return re_val;
	}
	
	
	//�жϵ�ǰ�ڵ�����޷�֧(������������)
	//���� true��     �з�֧
	//   false:  �޷�֧
	boolean hasBranch(String Id, Map<String, TerminalDataJD> termMap){
		boolean re_val;
		String afterStr = termMap.get(Id).getAfterTerm();
		
		if(afterStr!=null){    
			if(afterStr.split(",").length > 1){
				re_val = true;
			}else {
				re_val = false;
			}
		}else {
			re_val = false;
		}
		
		return re_val;
	}
	
	//�жϺ��ӽڵ��Ƿ��޹���
	boolean childIsNOFault(String Id, Map<String, TerminalDataJD> termMap){
		int i=0;
		boolean re_val;
		
		if(termMap.get(Id).getAfterTerm() != null){
			String [] afterTermStr = termMap.get(Id).getAfterTerm().split(",");
			
			for(i=0; i<afterTermStr.length; i++){
				TerminalDataJD afterNode = termMap.get(afterTermStr[i]);
				if((afterNode.getJDSOE_A()+afterNode.getJDSOE_B()+afterNode.getJDSOE_C())>=FAULT_PHASE){
					break;
				}
			}
			
			re_val = (i==afterTermStr.length) ? true : false;
		}else{
			re_val = false;
		}
		
		return re_val;
	}
	
	/**
	 * ������ȱ������˲������ж�
	 * @param termMap     ֻ���豸�ն˵�����
	 * @param headTerm    ����ͼͷ����ID���������豸��Ҳ�����������㣨-1����
	 * @param shortType   �ӵ����� (0 ˲ʱ   1 ����)
	 * @param faultRat    �ն��ϱ�����ֵ
	 * @param zeroCurrent ��Ʈ��ֵ
	 * @return faultStr   ���϶�λ(���ն��ö��ŷָ�)
	 */
	public String depthOrder(Map<String, TerminalDataJD> termMap, String headTerm, int JDType,
			double faultRat){
		
		Stack<TerminalDataJD> termStack = new Stack<TerminalDataJD>();
		TerminalDataJD TerminalDataJD = new TerminalDataJD();
		String faultStr = new String();//����һ������ͼ�����й��ϵ�
		termStack.push(termMap.get(headTerm));
		
		while(!termStack.isEmpty()){
			TerminalDataJD = termStack.pop();
			
			if(TerminalDataJD == null){
				continue;
			}
			//��ǰ�ڵ����豸�ն�������жϴ������򲻴������Ժ��ӽ��б���
			if(TerminalDataJD.getDiviceId() != "-1"){
				System.out.println("��ǰ�����նˣ�"+TerminalDataJD.getDiviceId());           //����ʹ��	
				System.out.println(TerminalDataJD.toString());                           //��ӡ�ն���Ϣ
				faultJudge(TerminalDataJD, termMap);//�����ж�
			}
			
				if(TerminalDataJD.getAfterTerm() != null){                               //��ǰ�ն��к��ӽڵ�
					String[] afterTermStr = TerminalDataJD.getAfterTerm().split(",");    //�ָ���ն˵��ַ���������
				
				for(int i=0; i<afterTermStr.length; i++){
					termStack.push(termMap.get(afterTermStr[i]));                      //��ǰ�ն˵ĺ��ӽڵ���ջ
				}
			}else{//��֧�߱�����������Ҷ�ӽڵ�
				//�����ϱ��ʴ�����ֵ��ȷ��Ϊ���ϵ�(Ԥ���ն�©��)
				if((TerminalDataJD.getFaultCount()+TerminalDataJD.getNoFaultCount())!=0){
				
					if (faultMissingJudge(TerminalDataJD, faultRat,JDType)) {                 //�ӵع���©���о�
						if (!firstFaultPoint.equals("")) {                             // !firstNOFaultPoint.equals("")
							//if(loadCurrentJudge(firstFaultPoint, termMap, JDType)){ //���ɵ����о�

								//���
								if(hasBranch(firstFaultPoint, termMap)){               //firstFaultPoint�з�֧
									if(childIsNOFault(firstFaultPoint, termMap)){      //��֧���޹���,���򲻴���
										faultStr += firstFaultPoint + ",";
										faultStr += "#" + ",";                         //���ϵ㶨λ��firstFaultPoint֮�󣨺�����֧��              
									}
								}else{
									faultStr += firstFaultPoint + ",";
									//��������<id,"">��id�ն˺�ȫ����·  ��վƽ̨�����˸ĳ�<id,"-2">
									if(firstNOFaultPoint.equals("")){
										faultStr += "-2" + ",";                        //���ϵ㶨λ��firstFaultPoint֮�󣨺������豸��
									}else{
										faultStr += firstNOFaultPoint + ",";
									}
								}
							//}			
						}
					}
				}	
				
				//һ����·������ɣ���ձ���Ĺ��ϵ�
				firstFaultPoint = "";//�����ַ�����"***LineFlag"��null����,��ʾ����֧�ߵ��޹�����·���ڱ���
				firstNOFaultPoint = "";
			}
		}
		
		//�������˱������û�й��ϵĴ���
		if(faultStr.equals("")){
			faultStr = "#" + ",#";
		}
		return faultStr;
	}
	
	//�ӵض�λ��װ����
	public String JDLocPack(Map<String, TerminalDataJD> termMap, String headTerm, int JDType,double faultRat){
		String location = null;
		
		//��������
		if(faultRat==0.0){
			faultRat = 0.5;
		}
			
		
		location = depthOrder(termMap, headTerm, JDType, faultRat);
		
		return location;
	}
}
