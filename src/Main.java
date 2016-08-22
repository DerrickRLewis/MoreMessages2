import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.HashMap;
import java.util.Map;


public class Main {

    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    Map m = new HashMap();


                    Session session = request.session();
                    String userName = session.attribute("userName");
                    String password = session.attribute("userPassword");
                    User user = users.get(userName);

                    if(user == null){
                        return new ModelAndView(m, "index.html");
                    }
                    else if(! password.equals(user.password)){
                        throw new Exception("Invalid login");

                    }
                    else {
                        m.put("name", user.getName());
                        m.put("messages", user.messages);
                        return new ModelAndView(m, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("userName");
                    String password = request.queryParams("userPassword");
                    User user = users.get(name);

                    if(user == null){
                        users.put(name, new User(name, password));
                    }

                    Session session = request.session();
                    session.attribute("userName", name);
                    session.attribute("userPassword", password);

                    response.redirect("/");
                    return "";
                })
        );

        /**
         * Does not work correctly
         */
        Spark.post(
                "/delete-message",
                ((request, response) -> {
                    Session session = request.session();
                    int id = Integer.parseInt((request.queryParams("messageNumber")));
                    String name = session.attribute("userName");
                    User user = users.get(name);
                    //user.messages.get(id - 1), user);
                    user.messages.remove(id - 1);


                    response.redirect("/");
                    return "";
                })
        );
/**
 * Does not work correctly
 */
        Spark.post(
                "/edit-message",
                ((request, response) -> {
                    Session session = request.session();
                    int id = Integer.parseInt(request.queryParams("messageIndex")) - 1;
                    String editMessage = request.queryParams("editedMessage");
                    String name = session.attribute("userName");
                    User user = users.get(name);
//                    messages.put(user.messages.get(id), user);

                    user.messages.remove((Integer) id);
                    user.messages.remove(id - 1);
                    user.messages.add(id, new Message(editMessage, id));

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/create-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    String message = request.queryParams("userMessage");

                    User user = users.get(name);
                    user.messages.add(new Message(message, user.id++));
                    session.attribute("userMessage", message);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
    }
}
