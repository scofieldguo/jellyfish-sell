package com.jellyfish.sell.support;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DataUtils {

	public static String percentum(Long data1,Long data2,Integer num) {
		StringBuffer sb =null;
		if(data2 == null ||data2 == 0L) {
			if(num == 0) {
				sb= new StringBuffer("0");
			}else {
				sb= new StringBuffer("0.");
			}
			 
			for(int i=0;i<num;i++) {
				sb.append("0");
			}
			return sb.toString();
		}
		BigDecimal bigData1 = new BigDecimal(data1);
		BigDecimal bigData2 = new BigDecimal(data2);
		BigDecimal rate  = bigData1.multiply(new BigDecimal(100)).divide(bigData2,2, RoundingMode.HALF_UP);
		return rate.toString();
	}

    public static String divide(Long data1, Long data2, Integer num) {
        StringBuffer sb = null;
        if (data2 == null || data2 == 0L) {
            if (num == 0) {
                sb = new StringBuffer("0");
            } else {
                sb = new StringBuffer("0.");
            }

            for (int i = 0; i < num; i++) {
                sb.append("0");
            }
            return sb.toString();
        }
        BigDecimal bigData1 = new BigDecimal(data1);
        BigDecimal bigData2 = new BigDecimal(data2);
        BigDecimal rate = bigData1.divide(bigData2, 2, RoundingMode.HALF_UP);
        return rate.toString();
    }
    public static Double multiplyDouble(Double a, Integer b){
        BigDecimal a1 = new BigDecimal(Double.toString(a.doubleValue()));
        BigDecimal b1 = new BigDecimal(Integer.toString(b.intValue()));
        return a1.multiply(b1).doubleValue();
    }

    public static Long  multiplyLong(Double a, Integer b) {
        BigDecimal a1 = new BigDecimal(Double.toString(a.doubleValue()));
        BigDecimal b1 = new BigDecimal(Integer.toString(b.intValue()));
        return a1.multiply(b1).longValue();
    }
    public static Double multiply(Double a, Integer b){
        BigDecimal a1 = new BigDecimal(Double.toString(a.doubleValue()));
        BigDecimal b1 = new BigDecimal(Integer.toString(b.intValue()));
        return a1.multiply(b1).doubleValue();
    }


    public static void main(String[] args) {
        System.out.println(divide(60L, 4L, 2));
    }
}
