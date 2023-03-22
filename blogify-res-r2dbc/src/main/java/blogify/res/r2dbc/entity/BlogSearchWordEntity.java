package blogify.res.r2dbc.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BLOG_SEARCH_WORD")
public class BlogSearchWordEntity implements Serializable {

	private static final long serialVersionUID = 2055049970316678736L;

	/**
	 * 일련번호
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	/**
	 * 단어
	 */
	private String word;

	/**
	 * 횟수
	 */
	private Long cnt;
}
