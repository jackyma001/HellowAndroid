package com.jacky;

public class ReviewSchedule {
	
	public static int GetNextReviewDate(int reviewTimes){
		int addedHours = 0;
		switch (reviewTimes){
			case 1: addedHours = 12;
			break;
			case 2: addedHours = 2*24;
			break;
			case 3: addedHours = 5*24;
			break;
			case 4: addedHours = 8*24;
			break;
			case 5: addedHours = 14*24;
			break;
		}
		return addedHours;
	}
}
