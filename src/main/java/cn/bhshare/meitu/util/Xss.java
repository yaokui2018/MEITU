package cn.bhshare.meitu.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Xss {

	public static void main(String[] args) {
		//经过编码后的XSS-Payload,<script>alert("xss");</script>
		String reqParam = "u003cu003eu003cu003e%3C%73%63%72%69%70%74%3E%61%6C%65%72%74%28%22%78%73%73%22%29%3B%3C%2F%73%63%72%69%70%74%3E";
		
		try {
			//解码后的新参数
			String newParam = URLDecoder.decode(reqParam,"UTF-8");
			//调用XSS过滤方法进行过滤后输出
			System.out.println(xssEncode(newParam));
			
		} catch (UnsupportedEncodingException e) {
			// 异常处理
			e.printStackTrace();
		}
		
		

	}
		
		public static String xssEncode(String s) {
			//XSS静态过滤方法
			if (s == null || s.isEmpty()) {
				return s;
			}
			StringBuilder sb = new StringBuilder(s.length() + 16);
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				switch (c) {
				case '>':
					sb.append('＞');// 全角大于号
					break;
				case '<':
					sb.append('＜');// 全角小于号
					break;
				case '\'':
					sb.append('‘');// 全角单引号
					break;
				case '\"':
					sb.append('“');// 全角双引号
					break;
				case '&':
					sb.append('＆');// 全角
					break;
				case '\\':
					sb.append('＼');// 全角斜线
					break;
				case '#':
					sb.append('＃');// 全角井号
					break;
				case '(':
					sb.append('（');//
					break;
				case ')':
					sb.append('）');//
					break;
				default:
					sb.append(c);
					break;
				}
			}
			return sb.toString();


	}

}
