package com.csg_chaohu_ground;

public class TerminalDataJD {

	//��Ա����
		public String diviceId;                                   //���ն˵�ID(-1Ϊ����ڵ㡣�����Ϊ�豸)
		public String beforeTerm;                                 //���ն�����ǰ�����ն�
		public String afterTerm;                                  //���ն����к󲿵��ն�(�����С��ʾ��֧��)
		//public double loadCurrent_A, loadCurrent_B, loadCurrent_C;//A��B��C����ĸ��ɵ���
		public int JDSOE_A, JDSOE_B, JDSOE_C;            //A��B��C����Ľӵ�SOE(0 �޹���soe  1 �й���soe)
		public int JDType;                                     //�ӵ����� (0 ��Դ�ӵ�   1 ��Դ�ӵ�)
		
		public int faultCount, noFaultCount;                      //�����й�����ͷǹ������ͳ��

		//��Ա����
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
