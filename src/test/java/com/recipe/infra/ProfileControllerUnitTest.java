package com.recipe.infra;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.*;

class ProfileControllerUnitTest {

    @Test
    void getRuntimeProfile() {
        //given
        String expectedProfile = "runtime";
        MockEnvironment mockEnvironment = new MockEnvironment();

        mockEnvironment.addActiveProfile(expectedProfile);
        mockEnvironment.addActiveProfile("mail");

        ProfileController controller = new ProfileController(mockEnvironment);

        //when
        String profile = controller.profile();

        //then
        assertEquals(profile, expectedProfile);
    }

    @Test
    void isEmptyRuntimeProfile() {
        //given
        String expectedProfile = "mail";
        MockEnvironment mockEnvironment = new MockEnvironment();

        mockEnvironment.addActiveProfile(expectedProfile);

        //when
        ProfileController profileController = new ProfileController(mockEnvironment);

        String profile = profileController.profile();

        //then
        assertEquals(profile, expectedProfile);
    }

    @Test
    void isEmptyActiveProfileInitDefault() {
        //given
        String expectedProfile = "local";
        MockEnvironment mockEnvironment = new MockEnvironment();

        ProfileController profileController = new ProfileController(mockEnvironment);

        //when
        String profile = profileController.profile();

        //then
        assertEquals(profile, expectedProfile);
    }

}