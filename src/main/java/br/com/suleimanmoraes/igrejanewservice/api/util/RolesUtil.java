package br.com.suleimanmoraes.igrejanewservice.api.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

public class RolesUtil {
	
	public static final String ROLE_ROOT = "ROLE_ROOT";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	public static Boolean temPermissao(String ...roles) {
		try {
			if(roles != null && roles.length > 0) {
				final SecurityContext context = SecurityContextHolder.getContext();
				if (context != null) {
					final Authentication authentication = context.getAuthentication();
					if(!CollectionUtils.isEmpty(authentication.getAuthorities())) {
						for(GrantedAuthority a : authentication.getAuthorities()) {
							for (int i = 0; i < roles.length; i++) {
								if(a.getAuthority().equals(roles[i])) {
									return Boolean.TRUE;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {}
		return Boolean.FALSE;
	}
	
	public static Boolean isRoot() {
		return temPermissao(ROLE_ROOT);
	}
	
	public static Boolean isAdmin() {
		return temPermissao(ROLE_ADMIN);
	}
}
