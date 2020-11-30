package com.techelevator.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.techelevator.model.models.AuthenticatedUser;
import com.techelevator.model.models.User;
import com.techelevator.model.models.UserCredentials;
import com.techelevator.model.models.review;
import com.techelevator.model.models.skatepark;
import com.techelevator.model.models.visit;
import com.techelevator.model.services.AuthenticationService;
import com.techelevator.model.services.AuthenticationServiceException;
import com.techelevator.model.view.ConsoleService;

public class App {

	// API Server base URL
	private static final String API_BASE_URL = "http://localhost:8080/";
 
	// Constants used for menu option passed to consoleService
    private static final String   MENU_OPTION_EXIT = "Exit";
    private static final String   LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String   LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private static final String   MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String   MAIN_MENU_OPTION_VIEW_PARKS = "View skateparks";
	private static final String   MAIN_MENU_OPTION_ADD_PARK = "Add a skatepark";
	private static final String   MAIN_MENU_OPTION_ADD_VISIT = "Record a visit to a skatepark";
	private static final String   MAIN_MENU_OPTION_ADD_REVIEW = "Leave a review of a skatepark";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_PARKS, MAIN_MENU_OPTION_ADD_PARK, 
														MAIN_MENU_OPTION_ADD_VISIT, MAIN_MENU_OPTION_ADD_REVIEW, 
														MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	//  Object to hold instance of the current user
	private AuthenticatedUser currentUser;       // Value assigned in login() method
	   
	// instance variables for services used in the App - assigned values in App contructor
    private ConsoleService console;
    private AuthenticationService authenticationService;
    
    private RestTemplate apiCall = new RestTemplate();		//Created our RestTemplate
    private Scanner aScanner = new Scanner(System.in);		//Created a new scanner 

  // Application program - main()
  //
  //     1. Instantiate the App
  //     2. Invoke the run() for the App to start processing
    
    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

   // App constructor with services dependency injected by Spring
   // All services should be listed as parameters and assigned to instance variable for the App
    
    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {   // Main program processing and looping
		startProgram();        // Perform program startup processing
		registerAndLogin();    // Login or register user
		mainMenu();            // display main menu and loop
		exitProgram();         // Perform end of program processing
	}

	private void mainMenu() {
		boolean shouldLoop = true;   // Loop control variable
		while(shouldLoop) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			
			switch(choice) {
				case MAIN_MENU_OPTION_LOGIN: {
					login();
					break;
				} 
				case MAIN_MENU_OPTION_VIEW_PARKS: {
					viewParks();
					break;
				}
				case MAIN_MENU_OPTION_ADD_PARK: {
					addPark();
					break;
				}
				case MAIN_MENU_OPTION_ADD_VISIT: {
					addVisit();
					break;
				}
				case MAIN_MENU_OPTION_ADD_REVIEW: {
					addReview();
					break;
				}
				default:  {              // the only other option on the main menu is to exit
					shouldLoop = false;  // set loop to exit
					break;
			}
		}
	}}
	
	/***************************************
	 * Methods to handle main menu options*
	 ***************************************/
	
	private void viewParks() {      
		// show a list of parks, including ID, park name, city, and state
		getAllParks();
		
		// prompt user to pick a park ID for more details
		System.out.println("To see more details, please select a park");
		String userParkChoiceInput = aScanner.nextLine();
		int userParkChoice = Integer.parseInt(userParkChoiceInput);
		skatepark chosenPark = getSkateparkById(userParkChoice);
		
		// display full park information
		System.out.println("Park Name: " + chosenPark.getParkName());
		System.out.println("Location: " + chosenPark.getCity() + ", " + chosenPark.getState());
		System.out.println("Outdoor/Indoor: " + chosenPark.getIndoorOutdoor());
		System.out.println("Are pads required? " + chosenPark.getPads());
		System.out.println("Notes: " + chosenPark.getNotes());
		
		// prompt user to choose to view reviews, visits, or return to the main menu
		System.out.println("To see all reviews for " + chosenPark.getParkName() + " press 1.");
		System.out.println("To see all visits for " + chosenPark.getParkName() + " press 2.");
		
		String reviewOrVisitChoiceInput = aScanner.nextLine();
		int reviewOrVisitChoice = Integer.parseInt(reviewOrVisitChoiceInput);
		
		if(reviewOrVisitChoice == 1 || reviewOrVisitChoice == 2) {
			if(reviewOrVisitChoice == 1) {
				getParkReviews(chosenPark.getParkId());
			} else {
				getParkVisits(chosenPark.getParkId());
			}
		} else {
			System.out.println("Please pick 1 for reviews, or 2 for visits");
		}
	}
	
	private void addPark() {
		// user will add a park to the database
		System.out.println("To add a new park, please answer the following questions");
		skatepark aSkatepark = new skatepark();
		System.out.println("What's the skatepark's called?");
		String userNameInput = aScanner.nextLine();
		aSkatepark.setParkName(userNameInput);
		System.out.println("What city is the park in?");
		String userCityInput = aScanner.nextLine();
		aSkatepark.setCity(userCityInput);
		System.out.println("What state is the park in?");
		String userStateInput = aScanner.nextLine();
		aSkatepark.setState(userStateInput);
		System.out.println("Is the park indoors or outdoors?");
		String userIndoorOutdoorInput = aScanner.nextLine();
		aSkatepark.setIndoorOutdoor(userIndoorOutdoorInput);
		System.out.println("Do you have to wear helmets or pads at this park?");
		String userPadsInput = aScanner.nextLine();
		aSkatepark.setPads(userPadsInput);
		System.out.println("Are there any notes you'd like to add?");
		String userNotesInput = aScanner.nextLine();
		aSkatepark.setNotes(userNotesInput);
		
		apiCall.postForEntity(API_BASE_URL + "park", makeParkEntity(aSkatepark), skatepark.class);
		
		System.out.println(aSkatepark.getParkName() + " has been added to your list of skateparks.");
	}
	
	private void addVisit() {
		// user can record a visit to a skatepark
		visit newVisit = new visit();
		
		newVisit.setSkaterId(currentUser.getUser().getId());
		
		getAllParks();
		System.out.println("To add a visit, please choose an ID from the park of your choice.");
		String userParkChoiceInput = aScanner.nextLine();
		int userParkChoice = Integer.parseInt(userParkChoiceInput);
		newVisit.setParkId(userParkChoice);
		
		LocalDate now = LocalDate.now();
		String currentDate = now.toString();
		newVisit.setVisitDate(currentDate);
		
		// INVOKE GET SKATEPARK BY ID METHOD TO CREATE A SKATEPARK OBJECT TO REFERENCE	
		apiCall.postForEntity(API_BASE_URL + "visit", makeVisitEntity(newVisit), visit.class);
		
		skatepark reviewedPark = getSkateparkById(newVisit.getParkId());
		
		System.out.println("You visited " + reviewedPark.getParkName() + " on " + newVisit.getVisitDate());	
	}
	
	private void addReview() {
		// user can add a review of an existing skatepark
		review newReview = new review();
		newReview.setSkaterId(currentUser.getUser().getId());
		
		getAllParks();
		System.out.println("To add a review, please choose an ID from the park of your choice.");
		String userParkChoiceInput = aScanner.nextLine();
		int userParkChoice = Integer.parseInt(userParkChoiceInput);
		newReview.setParkId(userParkChoice);
		
		System.out.println("On a scale of 1-5 what score would you give this park?");
		String userScoreInput = aScanner.nextLine();
		int userScore = Integer.parseInt(userScoreInput);
		newReview.setScore(userScore);
		
		System.out.println("Please leave your review of the park: (Limit 1000 characters)");
		String userReviewInput = aScanner.nextLine();
		newReview.setReview(userReviewInput);
		
		LocalDate now = LocalDate.now();
		String currentDate = now.toString();
		newReview.setVisitDate(currentDate);
						
		apiCall.postForEntity(API_BASE_URL + "review", makeReviewEntity(newReview), review.class);
		
		System.out.println("Thank you for your review!");
		
	}
	
	/***************************************
	 * Support methods *
	 ***************************************/
	
	private void getParkReviews(int parkId) {
				
		ResponseEntity<review[]> responseEntity = apiCall.getForEntity(API_BASE_URL + "review/" + parkId, review[].class);
		List<review> allParkReviews = Arrays.asList(responseEntity.getBody());
		
		
		for(review aReview : allParkReviews) {
			User reviewer = getUserById(aReview.getSkaterId());
			System.out.println("----------------------------------------");
			System.out.println("Username: " + reviewer.getUsername());
			System.out.println("Score: " + aReview.getScore());
			System.out.println("Review: " + aReview.getReview());
			System.out.println(aReview.getVisitDate());
			System.out.println();
		}
	}
	
	private void getParkVisits(int parkId) {
		
		ResponseEntity<visit[]> responseEntity = apiCall.getForEntity(API_BASE_URL + "visit/" + parkId, visit[].class);
		List<visit> allParkVisits = Arrays.asList(responseEntity.getBody());
		
		System.out.println("Date --- Skater");
		
		for (visit aVisit : allParkVisits) {
			User visitor = getUserById(aVisit.getSkaterId());
			System.out.println(aVisit.getVisitDate() + " - " + visitor.getUsername());
		}
	}
	
	private void getAllParks() {
		
		ResponseEntity<skatepark[]> responseEntity = apiCall.getForEntity(API_BASE_URL + "park", skatepark[].class);
		List<skatepark> allParks = Arrays.asList(responseEntity.getBody());
		System.out.println("Park ID - Name -------------- Location");
		
		for(skatepark aPark : allParks) {
			
			System.out.println(aPark.getParkId() + " " + aPark.getParkName() + " "
					+ "" + aPark.getCity() + ", " + aPark.getState());
		}
	}
	
	private skatepark getSkateparkById(int parkId) {
		skatepark aSkatepark = new skatepark();
		
		aSkatepark = apiCall.getForObject(API_BASE_URL + "park/" + parkId, skatepark.class);

		return aSkatepark;
	}
	
	private User getUserById(int userId) {
		User skater = apiCall.getForObject(API_BASE_URL + "skater/" + userId, User.class);
		return skater;
	}


	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			switch(choice) {
				case LOGIN_MENU_OPTION_LOGIN: {
					login();
					break;
				} 
				case LOGIN_MENU_OPTION_REGISTER: {
					register();
					break;
				} 
				default: {  // the only other option on the login menu is to exit
					exitProgram();
					break;
				}
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
	
	
	private void startProgram() { 
		System.out.println("*******************************");
		System.out.println("* SKATEPARK TRACKER ACTIVATED *");
		System.out.println("*******************************");
	}
	
	
	private void exitProgram() {

		System.out.println("*************************************************");
		System.out.println("* Thanks for using the Skatepark Tracker *");
		System.out.println("*************************************************");
		
		System.exit(0);  // Terminate programs with return code 0
	}
	
	// Methods to create a new HttpEntity so that we can send our new reviews and visits back to the server
	private HttpEntity<review> makeReviewEntity(review aReview) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//headers.setBearerAuth(currentUser.getToken());
		HttpEntity<review> entity = new HttpEntity<>(aReview, headers);
		return entity;
	}
	
	private HttpEntity<visit> makeVisitEntity(visit aVisit) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//headers.setBearerAuth(currentUser.getToken());
		HttpEntity<visit> entity = new HttpEntity<>(aVisit, headers);
		return entity;
	}
	
	private HttpEntity<skatepark> makeParkEntity(skatepark aPark) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//headers.setBearerAuth(currentUser.getToken());
		HttpEntity<skatepark> entity = new HttpEntity<>(aPark, headers);
		return entity;
	}
}
