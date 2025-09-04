package net.ludocrypt.limlib.impl.util;

import java.util.List;
import java.util.Map;

public class DebugUtil {
	private DebugUtil() {}

	public static String toDebugString(Map<String, List<String>> map) {
		if (map == null) {
			return "null";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			sb.append(' ')
				.append(entry.getKey())
				.append('=');

			List<String> values = entry.getValue();
			if (values == null) {
				sb.append("null,");
			} else if (values.isEmpty()) {
				sb.append("[],");
			} else {
				sb.append("[\n");
				for (String value : values) {
					sb.append(' ')
						.append(' ')
						.append(value)
						.append(',')
						.append('\n');
				}
				sb
					.append(' ')
					.append(']')
					.append(',');
			}
			sb.append('\n');
		}
		sb.append("}\n");
		return sb.toString();
	}
}
