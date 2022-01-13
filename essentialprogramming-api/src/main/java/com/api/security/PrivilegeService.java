package com.api.security;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@SuppressWarnings("unused")
public class PrivilegeService {

    public Set<String> getPrivilegeRoles(final String module){
        return Stream.of("visitor", "administrator")
                .collect(Collectors.toCollection(HashSet::new));
    }
}
