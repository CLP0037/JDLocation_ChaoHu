package com.csg_chaohu_ground;
//巢湖接地定位工程（和县短路定位基础更改）
/*   
* 1.算法根据线路的拓扑关系，对短路故障进行定位。
* 2.算法按深度优先遍历拓扑图，在遍历到每个终端时进行短路故障、故障漏报和负荷电流判断。
* 3.拓扑图的每条支线当做一条新的线路处理。
* 4.算法入参为只含终端的拓扑map<终端Id,终端数据>,终端数据见TerminalDataJD.java。根节点Id为-1表示虚拟节点(只有前后链路的数据)
*   否则均为设备 。根节点的beforeTerm和叶子节点的afterTerm均是空null。 
* 5.返回结果类型:
   #,#     无故障
   id1,id2 故障在id1与id2之间
   id,"-2" id终端后全部线路
   id,#    短信返回：id终端之后    标红：id终端之后到第一个任意节点  
*/
//CHLV1.0->CHLV1.1针对设备漏报可能多定位出故障点的情况作出修改

import java.util.*;

public class ChaoHuLocation {

	/**
	 * 获取程序版本号
	 */
	public void getVersion(){
		System.out.println("版本号：CHLV1.1");//
	}
	/**
	 * 短路定位
	 * @param terminalMap 只有设备终端的拓扑
	 * @param headID      拓扑图头结点的ID（可能是设备，也可能是虚拟结点（-1））
	 * @param JDType   接地类型 (0 无源接地   1 有源接地)
	 * @param faultRat    终端上报率阈值
	 * @return location   故障定位(两终端用逗号分隔)
	 */
	public String getJDLocation(Map<String, TerminalDataJD> terminalMap, String headID, int JDType,double faultRat){		
		LocationMethod locationMethod = new LocationMethod();
		
		getVersion();//读当前版本号
		String location = locationMethod.JDLocPack(terminalMap, headID, JDType, faultRat);
		System.out.println("接地定位:"+location);
		
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
