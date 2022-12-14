package com.expensify.expensify.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensify.expensify.dto.ActivityDTO;
import com.expensify.expensify.entity.Activity;
import com.expensify.expensify.entity.ExpenseStatus;
import com.expensify.expensify.entity.User;
import com.expensify.expensify.entity.expense.Expense;
import com.expensify.expensify.entity.split.Split;
import com.expensify.expensify.repository.ActivityRepository;
import com.expensify.expensify.service.ActivityService;
import com.expensify.expensify.service.UserService;

@Service
public class ActivityServiceImp implements ActivityService {

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	UserService userService;

	public ActivityDTO activityTOActivityDAO(Activity activity) {
		ActivityDTO activityDTO = new ActivityDTO();

		activityDTO.setAmount(activity.getAmount());
		activityDTO.setMessage(activity.getMessage());
		activityDTO.setUser(userService.UserToUserDTO(activity.getUser()));
		activityDTO.setExpense(activity.getExpense().getId());
		activityDTO.setTimestamp(activity.getTimestamp());
		return activityDTO;
	}

	@Override
	public List<ActivityDTO> getALLActivityOfUser(User user) {

		List<Activity> activities = activityRepository.findByUserOrderByTimestampDesc(user);

		List<ActivityDTO> activityDTOs = new ArrayList<>();

		for (Activity activity : activities) {
			activityDTOs.add(activityTOActivityDAO(activity));
		}

		return activityDTOs;

	}

	@Override
	public void createActivity(User user, Expense expense, Split split) {

		Activity activity = new Activity();

		if (split != null)
			activity.setAmount(split.getAmount());

		activity.setUser(user);
		activity.setExpense(expense);
		if (expense.getExpenseStatus() == ExpenseStatus.DELETED
				|| expense.getExpenseStatus() == ExpenseStatus.UPDATED) {
			activity.setMessage(messageConstructor2(user, expense, split));
		} else
			activity.setMessage(messageConstructor(user, expense, split));
		activityRepository.save(activity);
	}

	protected String messageConstructor(User user, Expense expense, Split split) {
		String message = "";
		if (user.getId() == expense.getExpensePaidBy().getId()) {
			message = "You created expense " + expense.getExpenseName();
			return message;
		}

		switch (expense.getExpenseType()) {
		case SETTLEUP:
			message = "You paid " + split.getAmount() + " by " + split.getUser().getUsername();
			break;

		default:
			message = "You added in " + expense.getExpenseName() + " by " + expense.getExpensePaidBy().getUsername();
			break;
		}

		return message;
	}

	protected String messageConstructor2(User user, Expense expense, Split split) {
		String message = "";

		switch (expense.getExpenseStatus()) {
		case DELETED:
			message = expense.getExpenseName() + " is deleted";
			break;
		case UPDATED:
			message = expense.getExpenseName() + " is deleted";
			break;

		default:
			message = "You added in " + expense.getExpenseName() + " by " + expense.getExpensePaidBy().getUsername();
			break;
		}

		return message;
	}

}
