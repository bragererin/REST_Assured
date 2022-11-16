package com.psrestassured;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // Add is you have any unrecognized fields
public class User {
    public String login;
    public int id;

    @JsonProperty("public_repos")
    public int publicRepos;

    public String getLogin()
    {
        return login;
    }

    public int getId()
    {
        return id;
    }

    public int getPublicRepos()
    {
        return publicRepos;
    }
}
