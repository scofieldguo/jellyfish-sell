package com.jellyfish.sell.support.stat;

import com.jellyfish.sell.support.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class BrowsLogBean implements Serializable {
    private static final long serialVersionUID = -4786815086084324677L;
    public static final int NEW=1;
	public static final int OLD=2;
	public static final int TYPE_VIEW=1;
	public static final int TYPE_LEFT=2;
	public static final int TYPE_ORDER=3;
	public static final int TYPE_BUY=4;
    private Date time;
    private Long userId;
    private Integer flag;
    private Long pid;
    private Integer aType;

    @Override
    public String toString() {
        StringBuffer sb=new StringBuffer("browsLog ");
        return sb.append(DateUtils.formatDate(time,"yyyy-MM-dd HH:mm:ss")).append("|").append(userId).append("|").append(this.flag).
        append("|").append(pid).append("|").append(this.aType).toString();
    }

}
