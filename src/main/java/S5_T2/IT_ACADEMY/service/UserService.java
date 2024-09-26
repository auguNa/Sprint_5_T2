package S5_T2.IT_ACADEMY.service;

import S5_T2.IT_ACADEMY.entity.UserEntity;

import java.util.List;


public interface UserService {

    UserEntity getUserSettings(String username);

    List<UserEntity> getAllUsers(String userDetails);

    void createUser(UserEntity userEntity);

    void updateUser(Long userId, UserEntity updatedUserEntity);

    void deleteUser(Long userId);

    UserEntity findByUsername(String username);
}
