package powerpowerplayv1;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
/**
 * Class Descrition
 *    -Downloaded this file from the googls sheets api documentation and modified it for my specific purposes. 
 * It takes login imformation from a file credetials_02.json and logs into an account which can then modify a google spreadsheet
 */
public class SheetsQuickstart{
  

  public static Sheets sheetsService;
  private static String ApplicationName = "Google Sheets API Java Quickstart";
  private static String spreadSheetID = "15AdFJ3W2Dqfe_mJE0vGU9HF4GB5EgKdo0yOtEJrSIaM";
  private static Credential authorize() throws IOException, GeneralSecurityException{
    InputStream in = SheetsQuickstart.class.getResourceAsStream("credentials_02.json");
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));

    List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), clientSecrets, scopes).setDataStoreFactory(new FileDataStoreFactory(new java.io.File("token"))).setAccessType("offline").build();
    Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    return credential;
  }
  public static Sheets getSheetsService() throws IOException, GeneralSecurityException{
    Credential credential = authorize();
    return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),GsonFactory.getDefaultInstance(), credential).setApplicationName(ApplicationName).build();

  }
  /**
   * addToDatabase-given a MatchData object, adds that compressed String version of that object to the google sheets
   */
  public static void addToDatabase(MatchData match){
    try{
      sheetsService =getSheetsService();
      ValueRange inputValues = new ValueRange().setValues(Arrays.asList(Arrays.asList(match.compress())));
      AppendValuesResponse appendResult = sheetsService.spreadsheets().values().append(spreadSheetID, "Sheet1", inputValues).setValueInputOption("USER_ENTERED").setInsertDataOption("INSERT_ROWS").setIncludeValuesInResponse(true).execute();
      
  
      
    }
    catch(Exception e){
      e.printStackTrace();
    }

  }
  /**
   * Reads every line in the sheets database and creates MatchData Objects from them; which in turn adds them to the static list in the MAtchData class
   */
  public static void readDatabase(){
      try{
      sheetsService =getSheetsService();

      ValueRange response = sheetsService.spreadsheets().values().get(spreadSheetID, "Sheet1").execute();

        List<List<Object>> values = response.getValues();
        for(List<Object> list: values)
        {
          for(Object obj: list){
            String str = String.valueOf(obj);
            MatchData match = new MatchData(str);//once a mathData object is created its constructor adds it to a static list in the class
          }
        }

    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}