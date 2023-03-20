package blogify.app.api.data;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class SearchBlogData implements Serializable {

	private static final long serialVersionUID = 5369958777485216587L;

	@NotEmpty
	private String query; // 검색을 원하는 질의어

	private String sort = "accuracy"; // 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy

	private Integer page = 1; // 결과 페이지 번호, 1~50 사이의 값, 기본 값 1

	private Integer size = 10; // 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10
}
