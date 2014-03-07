package com.rmrdevelopment.lifeleadership;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;

public class LLApplication extends Application {

	public static int skipcount;

	public static String username = "";
	public static String password = "";
	public static String Guid;
	public static int remember = 0;
	public static int userloggedin = 0;
	public static int flagSkipCount;

	public static int getFlagSkipCount() {
		return flagSkipCount;
	}

	public static void setFlagSkipCount(int flagSkipCount) {
		LLApplication.flagSkipCount = flagSkipCount;
	}

	public static int getSkipcount() {
		return skipcount;
	}

	public static void setSkipcount(int skipcount) {
		LLApplication.skipcount = skipcount;
	}

	public static int getUserloggedin() {
		return userloggedin;
	}

	public static void setUserloggedin(int userloggedin) {
		LLApplication.userloggedin = userloggedin;
	}

	public static ArrayList<HashMap<String, String>> stationLists = new ArrayList<HashMap<String, String>>();
	public static HashMap<String, String> audioInfo = new HashMap<String, String>();
	public static ArrayList<HashMap<String, String>> favstationLists = new ArrayList<HashMap<String, String>>();
	public static HashMap<String, String> stationInfo = new HashMap<String, String>();
	public static String totalTime = "";
	public static ArrayList<HashMap<String, String>> CommercialsLists = new ArrayList<HashMap<String, String>>();

	public static ArrayList<HashMap<String, String>> getCommercialsLists() {
		return CommercialsLists;
	}

	public static void setCommercialsLists(
			ArrayList<HashMap<String, String>> commercialsLists) {
		CommercialsLists = commercialsLists;
	}

	public static String getTotalTime() {
		return totalTime;
	}

	public static void setTotalTime(String totalTime) {
		LLApplication.totalTime = totalTime;
	}

	public static HashMap<String, String> getStationInfo() {
		return stationInfo;
	}

	public static void setStationInfo(HashMap<String, String> stationInfo) {
		LLApplication.stationInfo = stationInfo;
	}

	public static ArrayList<HashMap<String, String>> getFavstationLists() {
		return favstationLists;
	}

	public static void setFavstationLists(
			ArrayList<HashMap<String, String>> favstationLists) {
		LLApplication.favstationLists = favstationLists;
	}

	public static HashMap<String, String> getAudioInfo() {
		return audioInfo;
	}

	public static void setAudioInfo(HashMap<String, String> audioInfo) {
		LLApplication.audioInfo = audioInfo;
	}

	public static ArrayList<HashMap<String, String>> getStationLists() {
		return stationLists;
	}

	public static void setStationLists(
			ArrayList<HashMap<String, String>> stationLists) {
		LLApplication.stationLists = stationLists;
	}

	public static int getRemember() {
		return remember;
	}

	public static void setRemember(int remember) {
		LLApplication.remember = remember;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		LLApplication.username = username;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		LLApplication.password = password;
	}

	public static String getGuid() {
		return Guid;
	}

	public static void setGuid(String guid) {
		Guid = guid;
	}

	public static String UserId;

	public static String getUserId() {
		return UserId;
	}

	public static void setUserId(String userId) {
		UserId = userId;
	}
}
