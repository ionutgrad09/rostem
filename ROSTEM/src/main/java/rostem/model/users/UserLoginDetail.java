package rostem.model.users;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;


/////////////////////////////////////*
//
//  This class is not used!
//  All our models should inherit from UserDetails
//  Because of the getAuthorities that can be used with the principal
//
/////////////////////////////////////*
@Value
@Builder
@AllArgsConstructor
public class UserLoginDetail implements Serializable {
   private User user;
   private String role;
}
