package org.example.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",unique = true)
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    @Column(name = "age")
    @Min(value = 16, message = "MinAge should be greater than 16")
    @Max(value = 120, message = "MaxAge should be greater than 120")
    private byte age;

    @Column(name = "email")
    @NotEmpty(message = "Name should not be empty")
    @Email(message = "Email not correct")
    private String email;

    @Column(name = "login",unique = true)
    @NotEmpty(message = "login should not be empty")
    @Size(min = 2, max = 30, message = "login should be between 2 and 30 characters")
    private String login;

    @Column(name = "password")
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 3, message = "Password should be between min 3 characters")
    private String password;

    @ManyToMany
    private Set<Role> roles;

    public User() {
    }

    public User(String name, byte age, String email, String login, String password, Set<Role> roles) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    public User(String name, byte age, String email, String login, String password, Role... roles) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.login = login;
        this.password = password;
        this.roles = Set.of(roles);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
