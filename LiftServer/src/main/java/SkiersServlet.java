import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import org.json.JSONObject;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

@WebServlet(name = "SkiersServlet", value = "/SkiersServlet")
public class SkiersServlet extends HttpServlet {
  

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("text/plain");
    if (isValidated(request.getPathInfo())) {
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write("It works!");
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("Missing parameters");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("text/plain");
    String urlPath = request.getPathInfo();
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("Missing parameters");
    }

    String requestJson = request.getReader().lines().collect(Collectors.joining());
    if (!isValidated(urlPath) || !isJsonValidated(requestJson)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      response.setStatus(HttpServletResponse.SC_CREATED);
      response.getWriter().write("It works!");
    }
  }

  private boolean isValidated(String urlPath) {
    String urlPattern = "^/\\d+/seasons/\\d+/days/\\d+/skiers/\\d+$";
    Pattern pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
    return pattern.matcher(urlPath).matches();
  }

  private boolean isJsonValidated(String jsonRequest) {
    JSONObject jsonObject;
    try {
      jsonObject = new JSONObject(jsonRequest);
      Object timeObject = jsonObject.get("time");
      Object liftIDObject = jsonObject.get("liftID");
      return timeObject instanceof Integer && liftIDObject instanceof Integer;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
