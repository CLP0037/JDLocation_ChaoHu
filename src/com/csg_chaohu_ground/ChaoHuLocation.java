package com.csg_chaohu_ground;
//�����ӵض�λ���̣����ض�·��λ�������ģ�
/*   
* 1.�㷨������·�����˹�ϵ���Զ�·���Ͻ��ж�λ��
* 2.�㷨��������ȱ�������ͼ���ڱ�����ÿ���ն�ʱ���ж�·���ϡ�����©���͸��ɵ����жϡ�
* 3.����ͼ��ÿ��֧�ߵ���һ���µ���·����
* 4.�㷨���Ϊֻ���ն˵�����map<�ն�Id,�ն�����>,�ն����ݼ�TerminalDataJD.java�����ڵ�IdΪ-1��ʾ����ڵ�(ֻ��ǰ����·������)
*   �����Ϊ�豸 �����ڵ��beforeTerm��Ҷ�ӽڵ��afterTerm���ǿ�null�� 
* 5.���ؽ������:
   #,#     �޹���
   id1,id2 ������id1��id2֮��
   id,"-2" id�ն˺�ȫ����·
   id,#    ���ŷ��أ�id�ն�֮��    ��죺id�ն�֮�󵽵�һ������ڵ�  
*/
//CHLV1.0->CHLV1.1����豸©�����ܶඨλ�����ϵ����������޸�

import java.util.*;

public class ChaoHuLocation {

	/**
	 * ��ȡ����汾��
	 */
	public void getVersion(){
		System.out.println("�汾�ţ�CHLV1.1");//
	}
	/**
	 * ��·��λ
	 * @param terminalMap ֻ���豸�ն˵�����
	 * @param headID      ����ͼͷ����ID���������豸��Ҳ�����������㣨-1����
	 * @param JDType   �ӵ����� (0 ��Դ�ӵ�   1 ��Դ�ӵ�)
	 * @param faultRat    �ն��ϱ�����ֵ
	 * @return location   ���϶�λ(���ն��ö��ŷָ�)
	 */
	public String getJDLocation(Map<String, TerminalDataJD> terminalMap, String headID, int JDType,double faultRat){		
		LocationMethod locationMethod = new LocationMethod();
		
		getVersion();//����ǰ�汾��
		String location = locationMethod.JDLocPack(terminalMap, headID, JDType, faultRat);
		System.out.println("�ӵض�λ:"+location);
		
		return location;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Map<String, TerminalDataJD> termMap = new HashMap<String, TerminalDataJD>();
//		TerminalDataJD TerminalDataJDL1 = new TerminalDataJD();
//		TerminalDataJD TerminalDataJDL2 = new TerminalDataJD();
//		TerminalDataJD TerminalDataJDL3 = new TerminalDataJD();
//		TerminalDataJD TerminalDataJDL4 = new TerminalDataJD();
//		TerminalDataJD TerminalDataJDL5 = new TerminalDataJD();
//		TerminalDataJD TerminalDataJDL6 = new TerminalDataJD();
//		TerminalDataJD TerminalDataJDLA1 = new TerminalDataJD();
//		TerminalDataJD TerminalDataJDLA2 = new TerminalDataJD();
//		TerminalDataJD TerminalDataJDLA3 = new TerminalDataJD();
//		TerminalDataJD TerminalDataJDLB1 = new TerminalDataJD();
//		TerminalDataJD TerminalDataJDLB2 = new TerminalDataJD();
//		TerminalDataJDL1.diviceId = "L1";
//		TerminalDataJDL1.afterTerm="L2"; 
//		TerminalDataJDL2.diviceId = "L2";
//		TerminalDataJDL2.beforeTerm="L1"; TerminalDataJDL2.afterTerm="LA1,L3";
//		TerminalDataJDL3.diviceId = "L3";
//		TerminalDataJDL3.beforeTerm = "L2"; TerminalDataJDL3.afterTerm="L4";
//		TerminalDataJDL4.diviceId = "L4";
//		TerminalDataJDL4.beforeTerm = "L3"; TerminalDataJDL4.afterTerm="LB1,L5";
//		TerminalDataJDL5.diviceId = "L5";
//		TerminalDataJDL5.beforeTerm = "L4"; TerminalDataJDL5.afterTerm="L6";
//		TerminalDataJDL6.diviceId = "L6"; 
//		TerminalDataJDL6.beforeTerm = "L5";
//		TerminalDataJDLA1.diviceId = "LA1";
//		TerminalDataJDLA1.beforeTerm = "L2"; TerminalDataJDLA1.afterTerm="LA2"; 
//		TerminalDataJDLA2.diviceId = "LA2";
//		TerminalDataJDLA2.beforeTerm = "LA1"; TerminalDataJDLA2.afterTerm="LA3";
//		TerminalDataJDLA3.diviceId = "LA3";
//		TerminalDataJDLA3.beforeTerm = "LA2";
//		TerminalDataJDLB1.diviceId = "LB1";
//		TerminalDataJDLB1.beforeTerm = "L4"; TerminalDataJDLB1.afterTerm="LB2";
//		TerminalDataJDLB2.diviceId = "LB2";
//		TerminalDataJDLB2.beforeTerm = "LB1";		
//		TerminalDataJDL1.JDSOE_A=1;
////		TerminalDataJDL2.JDSOE_A=1;
////		TerminalDataJDL3.JDSOE_B=1;
//		TerminalDataJDL4.JDSOE_B=0;
//		TerminalDataJDL5.JDSOE_B=1;
////		TerminalDataJDL6.JDSOE_B=1;
////		TerminalDataJDLA1.JDSOE_B=1;
////		TerminalDataJDLA2.JDSOE_B=1;
//		TerminalDataJDLA3.JDSOE_B=1;
//		TerminalDataJDLB1.JDSOE_B=1;
////		TerminalDataJDLB2.JDSOE_B=1;
//
//		termMap.put("L1", TerminalDataJDL1);
//		termMap.put("L2", TerminalDataJDL2);
//		termMap.put("L3", TerminalDataJDL3);
//		termMap.put("L4", TerminalDataJDL4);
//		termMap.put("L5", TerminalDataJDL5);
//		termMap.put("L6", TerminalDataJDL6);
//		termMap.put("LA1", TerminalDataJDLA1);
//		termMap.put("LA2", TerminalDataJDLA2);
//		termMap.put("LA3", TerminalDataJDLA3);
//		termMap.put("LB1", TerminalDataJDLB1);
//		termMap.put("LB2", TerminalDataJDLB2);
//		
//		ChaoHuLocation JDLocation = new ChaoHuLocation();
//		JDLocation.getJDLocation(termMap, "L1", 1, 0.5);
	}

}
