package com.expensify.expensify.Controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensify.expensify.Exception.User.UserServiceException;
import com.expensify.expensify.dto.ActivityDTO;
import com.expensify.expensify.dto.DueAmountDTO;
import com.expensify.expensify.dto.ExpenseDTO;
import com.expensify.expensify.dto.FriendDTO;
import com.expensify.expensify.dto.JWTResponseDTO;
import com.expensify.expensify.dto.UserDTO;
import com.expensify.expensify.dto.UserLoginDTO;
import com.expensify.expensify.entity.Group;
import com.expensify.expensify.entity.User;
import com.expensify.expensify.entity.split.Split;
import com.expensify.expensify.service.ActivityService;
import com.expensify.expensify.service.DueAmountService;
import com.expensify.expensify.service.ExpenseService;
import com.expensify.expensify.service.UserService;
import com.expensify.expensify.utility.JWTUtility;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private DueAmountService dueAmountService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private JWTUtility jwtUtility;

	@PostMapping("/register")
	public JWTResponseDTO registerUser(@RequestBody @Valid UserDTO userModel) {
		User user = userService.createUser(userModel);

		final String token = jwtUtility.generateToken(user);

		return new JWTResponseDTO(token, userService.UserToUserDTO(user));
	}

	@PostMapping("/login")
	public JWTResponseDTO loginUser(@RequestBody @Valid UserLoginDTO userLoginDTO) throws Exception {

		User user = userService.userLogin(userLoginDTO);
		if (user == null) {
			throw new UserServiceException("INVALID_CREDENTIALS");
		}

		final String token = jwtUtility.generateToken(user);

		return new JWTResponseDTO(token, userService.UserToUserDTO(user));
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable("id") Long userId) {
		return userService.getUserById(userId);
	}

	@PostMapping("/update")
	private UserDTO updateUser(@AuthenticationPrincipal User user, @RequestBody @Valid UserDTO userdto) {
		return userService.updateUser(user, userdto);
	}

	@GetMapping("/youOwe")
	private int getYouOwe(@AuthenticationPrincipal User user) {
		return userService.youOwe(user);
	}

	@GetMapping("/youareOwe")
	private int getYouareOwed(@AuthenticationPrincipal User user) {
		return userService.YouareOwed(user);
	}

	@GetMapping("/current")
	public User getCurrentUser(@AuthenticationPrincipal User user) {
		return user;
	}

	@GetMapping("/myexpense")
	public List<ExpenseDTO> getAllExpenses(@AuthenticationPrincipal User user) {
		return expenseService.findAllExpenseOfUser(user.getId());
	}

	@GetMapping("/name/{name}")
	public User getUserByName(@PathVariable("name") String name) {
		return userService.loadUserByUserName(name);
	}

	@GetMapping("/all")
	public List<UserDTO> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/groups")
	public List<Group> getUserGroups(@AuthenticationPrincipal User user) {
		return userService.getUserGroups(user.getId());
	}

	@GetMapping("/settleUps")
	public List<DueAmountDTO> getALLSettleUpUserAmount(@AuthenticationPrincipal User user) {
		return dueAmountService.getAllDueAmountForm(user.getId());
	}

	@GetMapping("/friendExp")
	public List<FriendDTO> getALLFriendWithAmount(@AuthenticationPrincipal User user) {
		return userService.getALLFriendWithAmount(user);
	}

	@GetMapping("/settleUps/{id}")
	public DueAmountDTO getALLSettleUpUserFromAmount(@AuthenticationPrincipal User user, @PathVariable("id") Long id) {
		return dueAmountService.getAllDueAmountTo(user, id);
	}

	@GetMapping("/expenses/{id}")
	public List<Split> getUserExpenses(@PathVariable("id") Long userId) {
		return userService.getUserExpenses(userId);
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable("id") Long userId) {
		return userService.deleteUser(userId);
	}

	@GetMapping("/activity")
	public List<ActivityDTO> getUserActivity(@AuthenticationPrincipal User user) {
		return activityService.getALLActivityOfUser(user);
	}

	@GetMapping("/friend")
	public List<UserDTO> getALLFriends(@AuthenticationPrincipal User user) {
		return userService.getAllFriendList(user);
	}

	@PostMapping("/friend")
	public FriendDTO addFriend(@AuthenticationPrincipal User user, @RequestBody FriendDTO addFriendDTO) {
		return userService.addFriend(user, addFriendDTO);

	}
}
