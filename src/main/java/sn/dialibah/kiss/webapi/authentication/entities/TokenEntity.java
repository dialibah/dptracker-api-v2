package sn.dialibah.kiss.webapi.authentication.entities;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import sn.dialibah.kiss.webapi.common.models.EntityCore;

/**
 * by osow on 16/11/17.
 * for kiss-api
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "issuedToken")
@AllArgsConstructor
@Builder
public class TokenEntity extends EntityCore {

	@Indexed
	private String login;

	@Indexed(unique = true, expireAfterSeconds = 43200)
	private String jsonWebToken;

}
