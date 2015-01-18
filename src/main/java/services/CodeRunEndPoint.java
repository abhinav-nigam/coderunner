package services;

import com.coderunner.RunCodeRequest;
import com.coderunner.RunCodeResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


/**
 * Created by abhinavn on 18/1/15.
 */

@Endpoint
public class CodeRunEndPoint {
    private static Logger logger = Logger.getLogger(CodeRunEndPoint.class);
    private static final String NAMESPACE_URI = "http://www.coderunner.com";

    private CodeRunnerService codeRunnerService;

    @Autowired
    public CodeRunEndPoint(CodeRunnerService codeRunnerService) {
        this.codeRunnerService = codeRunnerService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "runCodeRequest")
    @ResponsePayload
    public RunCodeResponse runCode(@RequestPayload RunCodeRequest request) {
        RunCodeResponse response = new RunCodeResponse();
        logger.info("Running code: " + request.getCode());
        response.setCoderun(codeRunnerService.compileAndRun(request.getCode()));
        return response;
    }
}
