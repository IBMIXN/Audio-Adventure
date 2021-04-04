import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import handlers.CancelAndStopIntentHandler;
import handlers.EverythingIntentHandler;
import handlers.FallbackIntentHandler;
import handlers.HelpIntentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Config;
import watsonhandler.WatsonHandlerImpl;

/** Entry point for AWS Lambda. */
public class MainStreamHandler extends SkillStreamHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(EverythingIntentHandler.class);

  public MainStreamHandler() {
    super(getSkill());
  }

  private static Skill getSkill() {
    LOGGER.info("Skill Stream Handler Initialized");
    var config = getConfig();
    return Skills.standard()
        .withSkillId(config.alexaSkillId)
        .addRequestHandlers(
            new EverythingIntentHandler(new WatsonHandlerImpl(config)), // intent for
            // audio adventure
            new HelpIntentHandler(),
            new CancelAndStopIntentHandler(),
            new FallbackIntentHandler())
        .addRequestInterceptor(new NewSessionInterceptor())
        .addResponseInterceptor(new SaveSessionInterceptor())
        // Add persistent storage support
        .withAutoCreateTable(true)
        .withTableName("AudioAdventure")
        .build();
  }

  public static Config getConfig() {
    return Config.getConfigFromEnvironment();
  }
}
