package sn.dialibah.kiss.webapi.authentication.entities;


import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import sn.dialibah.kiss.webapi.common.models.EntityCore;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Document(collection = "profiles")
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEntity extends EntityCore {

	@Indexed(unique = true)
	private String login;

	private String password;

	private String firstName, lastName;
}
