package blogify.ext;

import lombok.Data;

@Data
public class TimeoutProperties {

	private Integer connect;

	private Integer read;

	private Integer write;
}
