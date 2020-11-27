import com.amazon.ask.AlexaSkill;
import com.amazon.ask.Skill;
import com.amazon.ask.builder.CustomSkillBuilder;
import com.amazon.ask.SkillStreamHandler;
import handlers.*;

/**
 * Entry point for AWS Lambda
 */
public class MainStreamHandler extends SkillStreamHandler {
    private static Skill getSkill() {
        return new CustomSkillBuilder()
                .withSkillId("temp")
                .addRequestHandlers(
                        new EverythingIntentHandler(),  // intent for audio adventure
                        new CancelIntentHandler(),
                        new FallbackIntentHandler(),
                        new HelpIntentHandler(),
                        new NavigateHomeIntentHandler(),
                        new StopIntentHandler()
                )
                .build();

    }

    public MainStreamHandler(AlexaSkill skill) {
        super(skill);
    }
}
