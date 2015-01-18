$(document).ready(function() {
    $("form#codeForm").on("submit", function(e){
        e.preventDefault();
        $.soap({
            url: 'http://'+ window.location.hostname +':8080/ws/',
            namespaceURL: 'http://www.coderunner.com',
            method: 'runCodeRequest',

            data: {
                code: $('textarea#code').val()
            },

            success: function (soapResponse) {
                obj = soapResponse.toJSON();
                $('textarea#output').val(obj.Body.runCodeResponse.coderun.output);
                $('textarea#error').val(obj.Body.runCodeResponse.coderun.error);
                return false;
            },
            error: function (soapResponse) {
                $('textarea#output').val("Server failure, try again later");
                return false;
            }
        });
    });
});
