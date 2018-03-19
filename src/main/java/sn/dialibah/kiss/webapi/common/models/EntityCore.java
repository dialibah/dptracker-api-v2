package sn.dialibah.kiss.webapi.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

import java.time.LocalDateTime;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public abstract class EntityCore {

	public EntityCore(String id,
	                  LocalDateTime creationDateTime,
	                  LocalDateTime lastModificationDateTime) {
		this.id = id;
		this.creationDateTime = creationDateTime;
		this.lastModificationDateTime = lastModificationDateTime;
	}

	@Id
	protected String id;

	@CreatedBy
	protected String createdBy;

	@CreatedDate
	protected LocalDateTime creationDateTime;

	@LastModifiedBy
	protected String lastModifiedBy;

	@LastModifiedDate
	protected LocalDateTime lastModificationDateTime;

}
