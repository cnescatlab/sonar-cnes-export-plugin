/*
 * This file is part of cnesexport.
 *
 * cnesexport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * cnesexport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with cnesexport.  If not, see <http://www.gnu.org/licenses/>.
 */

window.registerExtension('cnesexport/export', function (options) {
    // let's create a flag telling if the page is still displayed
    var isDisplayedExporting = true;

    /**
     * Log information in the bottom text area
     * @param string Text to log
     */
    var log = function (string) {
        // get the logging element
        var logging = document.querySelector('#logging');
        // append text to log
        logging.innerHTML = logging.innerHTML + "\n" + string;
        // scroll to bottom
        logging.scrollTop = logging.scrollHeight;
    };

    /**
     * Verify that the fields are correct.
     * @returns {boolean} true if all is good
     */
    var checkForm = function () {
        // check the field key
        // get it
        var key = document.forms["generation-form"]["key"].value;
        // check if void
        if (key === "") {
            // log error
            log("Key must be filled out.");
            // abort the process
            return false;
        }

        return true;
    };

    /**
     * Clear log information in text area
     */
    var clearLog = function () {
        // get the logging element
        var logging = document.querySelector('#logging');
        // set initial text to log
        logging.innerHTML = "## Logging console ##";
    };

    /**
     *  Lock or unlock the form
     *  @param isEnabled true to unlock, false to lock the form
     */
    var setEnabled = function (isEnabled) {
        // retrieve the form
        var form = document.getElementById("generation-form");
        // get all the components of the form
        var elements = form.elements;
        // change all components readOnly field to (un)lock them
        for (var i = 0, len = elements.length; i < len; i++) {
            elements[i].readOnly = !isEnabled;
            elements[i].disabled = !isEnabled;
        }

        if(isEnabled) {
            // hide loading when button are enabled
            $('#loading').hide();
        } else {
            // show loading otherwise
            $('#loading').show();
        }
    };

    /**
     * Generate the export
     * @param key
     * @param author
     */
    var produceExport = function (key) {
        // http GET request to the cnes web service
        window.SonarRequest.getJSON(
            '/api/cnesexport/export',
            { key: key }
        ).then(function (response) {

            // create a new link to download data
            var a = window.document.createElement('a');
            a.href = window.URL.createObjectURL(new Blob([response.logs], {type: 'text/csv'}));
            a.download = key+'.csv';

            // append link to body
            document.body.appendChild(a);
            a.click();
            // remove link from body
            document.body.removeChild(a);

            // on success log generation
            log("[INFO] CNES web service's response: \n" + response.logs);
            log("############################################################\n\tExport terminated!\n############################################################\n");
            setEnabled(true);
        }).catch(function (error) {
            // log error
            log("[ERROR] Export failed.");
            setEnabled(true);
        });
    };

    /**
     *  Get projects list from the server and fill out the combo box
     */
    var initProfilesDropDownList = function() {
        window.SonarRequest.getJSON(
            '/api/qualityprofiles/search'
        ).then(function (response) {
            // on success
            // we put each quality gate in the list
            $.each(response.profiles, function (i, item) {
                // we create a new option for each quality gate
                // in the json response
                var option = $('<option>', {
                    value: item.key,
                    text : item.name + ' [' + item.key + ']'
                });
                // we add it to the drop down list
                $('#key').append(option);
            });
        }).catch(function (response) {
            // log error
            error(response);
        });
    };

    // once the request is done, and the page is still displayed (not closed already)
    if (isDisplayedExporting) {

        // Add html template
        var template = document.createElement("div");
        template.setAttribute("id", "template");
        options.el.appendChild(template);
        // retrieve template from html
        $('#template').load('../../static/cnesexport/templates/exportForm.html', function(){
            // set generation button action
            // set its action on click
            document.querySelector('#generation').onclick = function () {

                // clear logs
                clearLog();

                // hide loading
                $('#loading').hide();

                // validation of the form
                if(checkForm()) {

                    // Get form values
                    var key = document.forms["generation-form"]["key"].value;

                    // lock the form
                    setEnabled(false);

                    // show loading
                    $('#loading').show();

                    // request the creation of the report
                    produceExport(key);
                }
            };

            // fill out project's drop down list
            initProfilesDropDownList();
        });

    }

    // return a function, which is called when the page is being closed
    return function () {
        // we unset the `isDisplayedExporting` flag to ignore to Web API calls finished after the page is closed
        isDisplayedExporting = false;
        // clean elements of this page
        options.el.textContent = '';
    };
});