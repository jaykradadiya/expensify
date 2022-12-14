package com.expensify.expensify.service;

import java.util.List;

import com.expensify.expensify.dto.FriendDTO;
import com.expensify.expensify.dto.UserDTO;
import com.expensify.expensify.dto.UserLoginDTO;
import com.expensify.expensify.entity.Group;
import com.expensify.expensify.entity.User;
import com.expensify.expensify.entity.split.Split;

public interface UserService {
	User UserDTOTOUser(UserDTO userDTO);

	UserDTO UserToUserDTO(User user);

	UserDTO updateUser(User user, UserDTO userDTO);

	User createUser(UserDTO userModel);

	User getUserById(Long userId);

	List<Group> getUserGroups(Long userId);

	List<Split> getUserExpenses(Long userId);

	List<UserDTO> getAllUsers();

	String deleteUser(Long userId);

	User userLogin(UserLoginDTO userLogin);

	User loadUserByUserName(String userName);

	List<UserDTO> getAllFriendList(User user);

	FriendDTO addFriend(User user, FriendDTO addFriendDTO);

	int youOwe(User user);

	int YouareOwed(User user);

	List<FriendDTO> getALLFriendWithAmount(User user);

}
