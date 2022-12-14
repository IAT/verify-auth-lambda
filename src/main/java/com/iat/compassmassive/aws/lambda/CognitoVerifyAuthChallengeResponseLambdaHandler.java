package com.iat.compassmassive.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

public class CognitoVerifyAuthChallengeResponseLambdaHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    public ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> event, Context context) {
        LambdaLogger log = context.getLogger();
        System.out.println("event at verify auth handler:" + event);
        System.out.println("event at verify auth handler:" + event.get("request"));
        log.log("event at define verify handler:" + event);
        Map<String, Object> request = (Map<String, Object>) event.get("request");
        Map<String, Object> response = (Map<String, Object>) event.get("response");

        try {
            String expectedAnswer = ((Map<String, Object>) request.get("privateChallengeParameters")).get("ANSWER").toString();
            String decodedAnswer = new String(Base64.getUrlDecoder().decode(expectedAnswer));
            //String decodedAnswer = URLDecoder.decode(request.get("challengeAnswer").toString(), "UTF-8");
            if (expectedAnswer.equals(decodedAnswer)) {
                response.put("answerCorrect", true);
            } else {
                response.put("answerCorrect", false);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return event;
    }
}

