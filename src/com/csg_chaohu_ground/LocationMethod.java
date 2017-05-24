package com.csg_chaohu_ground;

import java.util.*;

public class LocationMethod {
	
	final int PERMFAULT = 1;                        //永久性故障
	final int TEMPFAULT = 0;                        //瞬时性故障
	final int FAULT_PHASE = 1;                      //报SOE接地故障相数
	final double SIG_PHASE_CURUENT = 1.0;             //单相接地SOE负荷电流阈值   
	String firstFaultPoint="", firstNOFaultPoint="";//保存第一个故障点和第一个非故障点  
	String  headTermID = null;                      //保存拓扑图的第一个设备终端ID
	
	//从拓扑数据中去除非终端的数据，保留设备终端map
	public Map<String, TerminalDataJD> getTermMap(Map<String, TerminalDataJD> termMap, String headTerm){
		Map<String, TerminalDataJD> map_re = new HashMap<String, TerminalDataJD>();//返回终端Map		
		Stack<TerminalDataJD> termStack = new Stack<TerminalDataJD>();
		TerminalDataJD TerminalDataJD = new TerminalDataJD();
		boolean headTermFlag=true, hasBranch=false;
		
		termStack.push(termMap.get(headTerm));
		
		while(!termStack.isEmpty()){
			TerminalDataJD = termStack.pop();
			
			if(TerminalDataJD == null){
				continue;
			}
			//当前节点是设备终端则保留；否则去除，但对孩子进行遍历
			if(TerminalDataJD.getDiviceId() != "-1"){
				System.out.println("保留终端："+TerminalDataJD.getDiviceId());
				System.out.println(TerminalDataJD.toString());//打印终端信息
				if(map_re.isEmpty()){
					headTermID = TerminalDataJD.getDiviceId();
				}
				map_re.put(TerminalDataJD.getDiviceId(), TerminalDataJD);
			}else{
				//删除结点的后部结点指向前部结点     将去除节点的前一个终端ID，赋值给去除节点的后一个节点的beforeTerm。保存链路关系
				if(TerminalDataJD.getAfterTerm() != null){//当前终端是否有孩子节点
					String[] afterTermStr = TerminalDataJD.getAfterTerm().split(",");//分割后部终端的字符串成数组
					if(afterTermStr.length > 1){//有分支标志，仅为寻找头结点使用
						hasBranch = true;
					}
					for(int i=0; i<afterTermStr.length; i++){
						TerminalDataJD afterNode = termMap.get(afterTermStr[i]);
						afterNode.setBeforeTerm(TerminalDataJD.getBeforeTerm());
					}
				}
				//删除结点的前部结点指向后部结点
				if(TerminalDataJD.getBeforeTerm() != null){//前部应该只有一个结点
					map_re.get(TerminalDataJD.getBeforeTerm()).setAfterTerm(TerminalDataJD.getAfterTerm());
					System.out.println(map_re.get(TerminalDataJD.getBeforeTerm()).toString());//打印终端信息
				}
			}
			
			//寻找拓扑的头结点(可能是设备终端也可能是虚拟结点)
			if(headTermFlag && hasBranch && map_re.isEmpty()){
				headTermFlag = false;
				hasBranch = false;
				headTermID = TerminalDataJD.getDiviceId();//头结点ID
				map_re.put(TerminalDataJD.getDiviceId(), TerminalDataJD);
			}
			
			//对当前终端的孩子进行遍历
			if(TerminalDataJD.getAfterTerm() != null){//当前终端有孩子节点
				String[] afterTermStr = TerminalDataJD.getAfterTerm().split(",");//分割后部终端的字符串成数组
				
				for(int i=0; i<afterTermStr.length; i++){
					termStack.push(termMap.get(afterTermStr[i]));//当前终端的孩子节点入栈
				}
			}
		}
		
		return map_re;
	}	
	
	//终端接地SOE相数统计
	int getFSOECount(Map<String, TerminalDataJD> termMap, String termID){
		int count_re = -1;
		
		if(!termMap.containsKey(termID)){
			return count_re;
		}
		
		TerminalDataJD TerminalDataJD = termMap.get(termID);
		count_re = TerminalDataJD.getJDSOE_A() + TerminalDataJD.getJDSOE_B() + TerminalDataJD.getJDSOE_C();
		
		return count_re;
	}
	
	//故障判断逻辑  ========刷新到当前设备为止，前面的故障设备数和非故障设备数，以及从出站点到最后一个故障设备ID和非故障设备ID
	void faultJudge(TerminalDataJD TerminalDataJD, Map<String, TerminalDataJD> termMap){
		 
		int faultCountTemp, noFaultCountTemp;                                  //故障和非故障终端统计的零时变量
		//统计接地SOE计数
		int JDSOECount = TerminalDataJD.getJDSOE_A() + TerminalDataJD.getJDSOE_B() + TerminalDataJD.getJDSOE_C();
		
		if(TerminalDataJD.getBeforeTerm() != null){//取前一个终端的故障相统计
			if(termMap.containsKey(TerminalDataJD.getBeforeTerm())){
				faultCountTemp = termMap.get(TerminalDataJD.getBeforeTerm()).getFaultCount();
				noFaultCountTemp = termMap.get(TerminalDataJD.getBeforeTerm()).getNoFaultCount();
			}else{
				faultCountTemp = 0;
				noFaultCountTemp = 0;
			}
		}else{//前一个终端为空是头结点，故障终端统计为零
			faultCountTemp = 0;
			noFaultCountTemp = 0;
		}
	
		if(JDSOECount >= FAULT_PHASE){//报SOE的终端
			firstFaultPoint = TerminalDataJD.getDiviceId();                      //保存本线路的第一个故障点；
			firstNOFaultPoint = "";//null                                      //清空之前保存的非故障点，第一个非故障点应在本故障点之后		
			//故障终端统计
			faultCountTemp += 1;			
		}else if(JDSOECount==0){//没有报SOE的终端
			if(firstNOFaultPoint==""){//null                                   //***LineFlag
				firstNOFaultPoint = TerminalDataJD.getDiviceId();                // 保存本线路的第一个非故障点；
			}		
			//非故障终端统计 本终端后部结点只要有一个是报SOE的终端就计数
			if (TerminalDataJD.getAfterTerm() != null) {                         // 当前终端是否有孩子节点
				String[] afterTermStr = TerminalDataJD.getAfterTerm().split(",");// 分割后部终端的字符串成数组
				for(int i=0; i<afterTermStr.length; i++){
					TerminalDataJD afterNode = termMap.get(afterTermStr[i]);
					if((afterNode.getJDSOE_A()+afterNode.getJDSOE_B()+afterNode.getJDSOE_C())>=FAULT_PHASE){//不计数连续的未报故障的终端
						noFaultCountTemp += 1;//非故障终端统计
						break;
					}
				}
			}
		}
		
		//将故障终端统计计数保存到本终端对象(TerminalDataJD)
		TerminalDataJD.setFaultCount(faultCountTemp);
		TerminalDataJD.setNoFaultCount(noFaultCountTemp);
	}
	
	/**
	 * 防接地SOE漏报判据
	 * @param TerminalDataJD
	 * @param faultRat 终端上报率阈值
	 * @param JDType 接地类型 (0 无源接地   1 有源接地)
	 * @return true  故障上报率大于等于阈值  否则false 
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
	    

		if(TerminalDataJD.getFaultCount()>=2)//到上报故障的最后一个设备===报故障设备数量
		{
			re_val = true;
		}
		else
		{
			if((TerminalDataJD.getFaultCount()+TerminalDataJD.getNoFaultCount())==1)//到上报故障的最后一个设备=====设备总数
			{
				re_val = true;
			}
			else
				re_val = false;
		}
		
	    return re_val;
	}
	
	
	//判断当前节点后有无分支(至少两个孩子)
	//返回 true：     有分支
	//   false:  无分支
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
	
	//判断孩子节点是否无故障
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
	 * 深度优先遍历拓扑并进行判断
	 * @param termMap     只有设备终端的拓扑
	 * @param headTerm    拓扑图头结点的ID（可能是设备，也可能是虚拟结点（-1））
	 * @param shortType   接地类型 (0 瞬时   1 永久)
	 * @param faultRat    终端上报率阈值
	 * @param zeroCurrent 零飘阈值
	 * @return faultStr   故障定位(两终端用逗号分隔)
	 */
	public String depthOrder(Map<String, TerminalDataJD> termMap, String headTerm, int JDType,
			double faultRat){
		
		Stack<TerminalDataJD> termStack = new Stack<TerminalDataJD>();
		TerminalDataJD TerminalDataJD = new TerminalDataJD();
		String faultStr = new String();//保存一个拓扑图的所有故障点
		termStack.push(termMap.get(headTerm));
		
		while(!termStack.isEmpty()){
			TerminalDataJD = termStack.pop();
			
			if(TerminalDataJD == null){
				continue;
			}
			//当前节点是设备终端则进行判断处理；否则不处理，但对孩子进行遍历
			if(TerminalDataJD.getDiviceId() != "-1"){
				System.out.println("当前处理终端："+TerminalDataJD.getDiviceId());           //测试使用	
				System.out.println(TerminalDataJD.toString());                           //打印终端信息
				faultJudge(TerminalDataJD, termMap);//故障判断
			}
			
				if(TerminalDataJD.getAfterTerm() != null){                               //当前终端有孩子节点
					String[] afterTermStr = TerminalDataJD.getAfterTerm().split(",");    //分割后部终端的字符串成数组
				
				for(int i=0; i<afterTermStr.length; i++){
					termStack.push(termMap.get(afterTermStr[i]));                      //当前终端的孩子节点入栈
				}
			}else{//本支线遍历结束，是叶子节点
				//故障上报率大于阈值才确认为故障点(预防终端漏报)
				if((TerminalDataJD.getFaultCount()+TerminalDataJD.getNoFaultCount())!=0){
				
					if (faultMissingJudge(TerminalDataJD, faultRat,JDType)) {                 //接地故障漏报判据
						if (!firstFaultPoint.equals("")) {                             // !firstNOFaultPoint.equals("")
							//if(loadCurrentJudge(firstFaultPoint, termMap, JDType)){ //负荷电流判据

								//输出
								if(hasBranch(firstFaultPoint, termMap)){               //firstFaultPoint有分支
									if(childIsNOFault(firstFaultPoint, termMap)){      //分支都无故障,否则不处理
										faultStr += firstFaultPoint + ",";
										faultStr += "#" + ",";                         //故障点定位到firstFaultPoint之后（后面多分支）              
									}
								}else{
									faultStr += firstFaultPoint + ",";
									//返回类型<id,"">：id终端后全部线路  主站平台处理不了改成<id,"-2">
									if(firstNOFaultPoint.equals("")){
										faultStr += "-2" + ",";                        //故障点定位到firstFaultPoint之后（后面无设备）
									}else{
										faultStr += firstNOFaultPoint + ",";
									}
								}
							//}			
						}
					}
				}	
				
				//一条线路遍历完成，清空保存的故障点
				firstFaultPoint = "";//赋空字符串与"***LineFlag"处null区别,表示其他支线的无故障线路不在保存
				firstNOFaultPoint = "";
			}
		}
		
		//整个拓扑遍历完后，没有故障的处理
		if(faultStr.equals("")){
			faultStr = "#" + ",#";
		}
		return faultStr;
	}
	
	//接地定位封装方法
	public String JDLocPack(Map<String, TerminalDataJD> termMap, String headTerm, int JDType,double faultRat){
		String location = null;
		
		//参数保护
		if(faultRat==0.0){
			faultRat = 0.5;
		}
			
		
		location = depthOrder(termMap, headTerm, JDType, faultRat);
		
		return location;
	}
}
