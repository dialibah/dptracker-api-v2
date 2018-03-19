package sn.dialibah.dptracker.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@MappedSuperclass
@Inheritance
@DiscriminatorColumn(name = "ENTITY_CORE")
public abstract class EntityCore implements Serializable {

	public EntityCore(int id, LocalDateTime creationDateTime, LocalDateTime lastModificationDateTime) {
		this.id = id;
		this.creationDateTime = creationDateTime;
		this.lastModificationDateTime = lastModificationDateTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected int id;

	@CreatedBy
	protected String createdBy;

	@CreatedDate
	protected LocalDateTime creationDateTime;

	@LastModifiedBy
	protected String lastModifiedBy;

	@LastModifiedDate
	protected LocalDateTime lastModificationDateTime;

}
