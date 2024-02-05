package com.example.springOauth2.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = CustomUserDetailDeserializer.class)
public class CustomUserDetail implements UserDetails {

    String username;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("simple_gra"), new SimpleGrantedAuthority("nted_authority"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.username;
    }

    /*
     * TRUE: user not expired
     * FALSE: user expired
     */
    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * TRUE: user not locked
     * FALSE: user locked
     */
    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
    
}

class CustomUserDetailDeserializer extends JsonDeserializer<CustomUserDetail> {
    
    @Override
    public CustomUserDetail deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException, JacksonException {
        CustomUserDetail customUserDetail = CustomUserDetail.builder().username("moarna").build();
        return customUserDetail;
    }
}
