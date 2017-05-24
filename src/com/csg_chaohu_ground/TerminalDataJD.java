package com.csg_chaohu_ground;

public class TerminalDataJD {

	//成员变量
		public String diviceId;                                   //本终端的ID(-1为虚拟节点。否则均为设备)
		public String beforeTerm;                                 //本终端所有前部的终端
		public String afterTerm;                                  //本终端所有后部的终端(数组大小表示分支数)
		//public double loadCurrent_A, loadCurrent_B, loadCurrent_C;//A、B、C三相的负荷电流
		public int JDSOE_A, JDSOE_B, JDSOE_C;            //A、B、C三相的接地SOE(0 无故障soe  1 有故障soe)
		public int JDType;                                     //接地类型 (0 无源接地   1 有源接地)
		
		public int faultCount, noFaultCount;                      //拓扑中故障相和非故障相的统计

		//成员方法
		public TerminalDataJD(){
			JDSOE_A = 0;
			JDSOE_B = 0;
			JDSOE_C = 0;
			JDType  = -65535;
		}

		public String getDiviceId() {
			return diviceId;
		}

		public void setDiviceId(String diviceId) {
			this.diviceId = diviceId;
		}

		public String getBeforeTerm() {
			return beforeTerm;
		}

		public void setBeforeTerm(String beforeTerm) {
			this.beforeTerm = beforeTerm;
		}

		public String getAfterTerm() {
			return afterTerm;
		}

		public void setAfterTerm(String afterTerm) {
			this.afterTerm = afterTerm;
		}

//		public double getLoadCurrent_A() {
//			return loadCurrent_A;
//		}

//		public void setLoadCurrent_A(double loadCurrent_A) {
//			this.loadCurrent_A = loadCurrent_A;
//		}
//
//		public double getLoadCurrent_B() {
//			return loadCurrent_B;
//		}
//
//		public void setLoadCurrent_B(double loadCurrent_B) {
//			this.loadCurrent_B = loadCurrent_B;
//		}
//
//		public double getLoadCurrent_C() {
//			return loadCurrent_C;
//		}
//
//		public void setLoadCurrent_C(double loadCurrent_C) {
//			this.loadCurrent_C = loadCurrent_C;
//		}

		public int getJDSOE_A() {
			return JDSOE_A;
		}

		public void setJDSOE_A(int JDSOE_A) {
			this.JDSOE_A = JDSOE_A;
		}

		public int getJDSOE_B() {
			return JDSOE_B;
		}

		public void setJDSOE_B(int JDSOE_B) {
			this.JDSOE_B = JDSOE_B;
		}

		public int getJDSOE_C() {
			return JDSOE_C;
		}

		public void setJDSOE_C(int JDSOE_C) {
			this.JDSOE_C = JDSOE_C;
		}

		public int getJDType() {
			return JDType;
		}

		public void setJDType(int JDType) {
			this.JDType = JDType;
		}

		public int getFaultCount() {
			return faultCount;
		}

		public void setFaultCount(int faultCount) {
			this.faultCount = faultCount;
		}

		public int getNoFaultCount() {
			return noFaultCount;
		}

		public void setNoFaultCount(int noFaultCount) {
			this.noFaultCount = noFaultCount;
		}

		@Override
		public String toString() {
			return "TerminalDataJD [diviceId=" + diviceId
					+ ", beforeTerm=" + beforeTerm + ", afterTerm=" + afterTerm
					+ ", JDSOE_A=" + JDSOE_A + ", JDSOE_B=" + JDSOE_B
					+ ", JDSOE_C=" + JDSOE_C + ", JDType=" + JDType
					+ ", faultCount=" + faultCount + ", noFaultCount="
					+ noFaultCount + "]";
		}
}
