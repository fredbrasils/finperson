package br.com.finperson.security.domain;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.finperson.domain.BaseEntity;
import br.com.finperson.domain.UserEntity;
import br.com.finperson.domain.enumm.TypeEmailEnum;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
@NoArgsConstructor
@Entity
public class TokenEntity extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private static final int EXPIRATION = 60 * 24;
 
	public TokenEntity(final Long id) {
        super(id);
    }
	
    public TokenEntity(final Long id, final String token,TypeEmailEnum typeEmail) {
    	super(id);

        this.token = token;
        this.typeEmail = typeEmail;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
	
	@Builder
    public TokenEntity(final Long id,final String token, final UserEntity user,TypeEmailEnum typeEmail) {
    	super(id);

        this.token = token;
        this.user = user;
        this.typeEmail = typeEmail;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
	
    private String token;
   
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;
     
    private Date expiryDate;
    
    @Enumerated(EnumType.STRING)
    private TypeEmailEnum typeEmail;
    
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

}
