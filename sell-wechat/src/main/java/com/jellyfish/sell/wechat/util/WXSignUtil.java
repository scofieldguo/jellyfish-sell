package com.jellyfish.sell.wechat.util;

import com.jellyfish.sell.support.Md5EncryptUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WXSignUtil {
	public static String sign(Map<String, Object> paramValues, String key) {
		try {
			StringBuilder sb = new StringBuilder();
			List<String> paramNames = new ArrayList<String>(paramValues.size());
			paramNames.addAll(paramValues.keySet());
			Collections.sort(paramNames);
			for (String paramName : paramNames) {
				sb.append(paramName).append("=")
						.append(paramValues.get(paramName)).append("&");
			}
			String stringSignTemp = sb + "key=" + key;
			return Md5EncryptUtil.md5Enc(stringSignTemp,"UTF-8").toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCZtM/+mXLjaQTSNZdLpjK5yEoqMT1a8IWaW+t6yhVlwljTtugk8HGcPTorDvG/4LfP8yn6tm8VtL482F8T62GZC9zR6YO/xS27hUXgPpiUvkGNeuiMnH7RBSMJGLcaAAtI6PaH9EJLYtdfE753yCOhgQ0xezjwFtaoB0aDcCzIzdRzzE7Zf35/L+UgKBGOAB0ASmixUYRMg41Ml6Fl9YBYj0ELZ71fa89jvdO/vJRZ/6BISrK38MQkYoQL//fVmuEpOzgMYzZvLdGtdvHxspbrzhTZZvHDyUSvPY32ubZB1I51pHYyCJb4bTKiRXSvahLLpsUZETli4538/zVx4DFJAgMBAAECggEAFuGPxPra23eavCA2MZDAJF/oindiBtOfT+c3GsRSgx93Uaz8yeLxRqzmp9pFqqatNkugiC34Q2bwmuYq4TA9Cyd069qzPi0ALdYdHNC0rg/Uelc9nxD83+i/2m7PcxNmmqfzazG1XLuODB1IUVR92WD4sdECHNuJgOH0YUSlpg6hV8ZpKTDcTLfnOdQfL9+jKuBe6jUmpqgFXo2ddYLe5rVcaWxsSKXku5onH3CdzOk73vOhpEYAvcLrbV2d24L+gKUl3yXMrqO5yaYestd0dC39H6SbJHV75Ac/CnqcvgWs2ZZoOYMarDsPLzzETBld+NC0LDlpZY6gKj/NAFEWcQKBgQD6CAQUfufF9UfxHwKr6hKpTHPzrPv4yApEAMlbHQo5C2qjvEEs2jwQF2iCKZni3YmtQx3wx2BPi9mo9J6C3QTY2CO2OYKSKq09+WZDNyq830amMJiq6KXQixo7GXs/+xY65sr85FnXyWwIvgn5SRNVpzn3rRY9/EodLv2sFxR9swKBgQCdYCNPQGeBLstnuyd4N/QW9Lc8lc72X3UWUozHTg2x5nv3FTCzcbYqh3Sha4c2pEQtX6oQxJhNCB35aub83bGVmpLgEi16RYqJfHAwHtEiD9LddJhA80NaR/PM/l5JIF6GzGBNHfFkaOXIvgceYa1HAPug5NqciwVAwWlc2R0vEwKBgQDCtVNYEquzgRuRLjEKZ7I4nQz/Cgr+5ej4pBQqHCdF3RoigAvRPfHgJhqAopzwCs2YW9gJFXpu9918sWJvTmbj5w2Ibqi74vgedwWn+mIRy15dNSLulVXrXSgENgfA/2uEWw5gegNtA7JZ7IQf2URxQ1TlB6l2g+3DZECvrxmJ+QKBgB7SyZeoIZ2nsKcsfNqK+v/UBl+TZ+nRYHlyyafvnbcOUZFfUR6UUv2Zcjp7eQw2uDO63PBT0RClWlmTmpJESZfnooTIiqe8Hxo2iTKjlop5Vu+Q+pEeFD8GgLXRzF2+PUVhmisAhlsIQJlEvtgwgWO6yY4FxNuMTwcT2rMYwyKJAoGAXHLXTPhZx0LKuErInQDJZwbJo7Yg8T2820OpjqidDziCPFWZBvsddQVx/JzB08RSpSUCuAEVLUHRWpgqCxE29BtfdAlQr8IKXREG/dQ8MxHHCanaWeKihIhCGNEnz6FV2nBnlGwbQ8hDS2h0bN175lD0Q8c2+TzK2MZW3qPEtdU=".length());
	}
}
