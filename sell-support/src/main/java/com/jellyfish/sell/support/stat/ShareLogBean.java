package com.jellyfish.sell.support.stat;

import com.jellyfish.sell.support.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class ShareLogBean implements Serializable {
    private static final long serialVersionUID = 3063265357581958197L;
    public static final int NEW=1;
	public static final int OLD=2;

	public static final int TYPE_CALLBACK=1;
	public static final int TYPE_HELP=2;
	public static final int TYPE_PRODUCT=3;
	public static final int TYPE_APP=4;
	public static final int TYPE_SAVEPICTURE=5;

    /**
     * 商品ID
     */
    private Long  pId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户标识
     */
    private Integer flag;
    /**
     * 时间
     */
    private Date time;

    /**
     * 类型
     */
    private Integer type;

    public ShareLogBean(){
}

	@Override
    public String toString() {
        StringBuffer sb=new StringBuffer("shareLog ");
        return sb.append(DateUtils.formatDate(time,"yyyy-MM-dd HH:mm:ss")).append("|")
                .append(userId).append("|").append(this.flag).append("|").append(type).append("|").append(pId).toString();
    }
}
