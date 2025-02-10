package edu.uga.devdogs.course_information.controller;

import edu.uga.devdogs.course_information.Building.Building;
import edu.uga.devdogs.course_information.Course.Course;
import edu.uga.devdogs.course_information.CourseSection.CourseSection;
import edu.uga.devdogs.course_information.service.CourseInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Collections;
import java.util.List;


/**
 * Controller that handles the Course-Information details.
 * Provides courses based on parameters like major, professor,
 * CRN, time slot, and athena name.
 */
@Tag(name="Course Information API", description="Uses the Course PDF to provide detailed course data")
@RestController
@RequestMapping("/api/courseInformation")
public class CourseInfoController {

    //inject our Course Information PDFs microservice into our REST controller
    private final CourseInformationService courseInformationService;

    @Autowired
    public CourseInfoController(CourseInformationService courseInformationService) {
        this.courseInformationService = courseInformationService;
    }

   
    /**
     * Asks for list of course information that relates to the 
     * @param professor given.
     * 
     * @param professor name of the professor teaching the course
     * @return course information list that's related to the given professor.
     */
    @Operation(summary = "get list of courses by professor", description = "Retrieces course information relating to the professor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Section found"),
        @ApiResponse(responseCode = "400", description = "Invalid CRN"),
        @ApiResponse(responseCode = "404", description = "Section not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/professor")
    @Tag(name="course-information")
   public ResponseEntity<List<CourseSection>> getCourseByProfessor(@RequestParam(value = "professor",required = true) String professor){
        
     if(professor == null || professor.isEmpty()){
          return ResponseEntity.badRequest().body(null); //Returns 400 if the parameter is not provided
     }

     try {
          List<CourseSection> courseInfo = courseInformationService.getCourseSectionsByProfessor(professor); //Fetches course information based on professor
          
          if(courseInfo.isEmpty()){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(courseInfo); //Returns 404 error code is course info isn't found
          }

          return ResponseEntity.ok(courseInfo);
     } catch (Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
     }
   } 
    


    /**
     * Retrieves a list of courses for a given major
     * 
     * @param major The major identifier for which to select courses (e.g. CSCI)
     * @return a list of Course objects matching the given major
     */
    @Operation(summary = "Get courses by major", description = "Retrieves a list of course objects with the given major identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Section found"),
        @ApiResponse(responseCode = "400", description = "Invalid CRN"),
        @ApiResponse(responseCode = "404", description = "Section not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/coursesByMajor")
    @Tag(name="course-information")
    public ResponseEntity<List<Course>> getCoursesByMajor(@RequestParam String major) {

        //return 400 for empty major
        if (major.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            //Call method to get course list by major
            List<Course> courseList = courseInformationService.getCoursesByMajor(major);

            //Check if the above method call returned null
            if (courseList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //Return 404 if no courses are found
            }

            //Return the courses if found
            return ResponseEntity.ok(courseList);

        } catch (Exception e) {

            //Return 500 if a server error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Returns the details of a specified CRN
     * 
     * @param crn The CRN of the section
     * @return returns a section object for the CRN
     */
    @Operation(summary = "get section by crn", description = "Retrieves a section from the given CRN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Section found"),
        @ApiResponse(responseCode = "400", description = "Invalid CRN"),
        @ApiResponse(responseCode = "404", description = "Section not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/section-by-crn")
    @Tag(name="course-information")
    public ResponseEntity<CourseSection> getCourseEntity(@RequestParam String crn) {

        //return 400 for empty CRN
        if (crn.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            //Call method to get section details
            CourseSection sectionDetails = courseInformationService.getSectionDetailsByCrn(crn);

            //Check if the above method call returned null
            if (sectionDetails == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //Return 404 if no courses are found
            }

            //Return the section if found
            return ResponseEntity.ok(sectionDetails);

        } catch (Exception e) {

            //Return 500 if a server error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    /**
     * Retrieves course information based on Athena name.
     * 
     * @param athenaName The Athena name of the course
     * @return Course details for the given Athena name
     */
    @Operation(summary = "Get course by Athena name", description = "Retrieves course List based on the provided Athena name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Section found"),
        @ApiResponse(responseCode = "400", description = "Invalid CRN"),
        @ApiResponse(responseCode = "404", description = "Section not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/course-by-athena-name")
    @Tag(name="course-information")
    public ResponseEntity<List<Course>> getCourseByAthenaName(@RequestParam String athenaName) {

        // Return 400 for empty athenaName
        if (athenaName.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            // Call a service method to fetch the course by Athena name
            List<Course> courseDetails = courseInformationService.getCourseByAthenaName(athenaName);  // not yet implemented

            // Check if the above method call returned null
            if (courseDetails == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // return 404 if no course is found
            }

            // Return the course details if found
            return ResponseEntity.ok(courseDetails);

        } catch (Exception e) {
            // Return 500 if a server error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get all buildings", description = "Retrieves a list of all building objects.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buildings found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/buildings")
    @Tag(name="course-information")
    public ResponseEntity<List<Building>> getAllBuildings() {
        try {
            List<Building> buildings = courseInformationService.getAllBuildings();
            return ResponseEntity.ok(buildings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Gets the special types of a course (honors/lab/online) from a CRN.
     * @param crn The CRN of the course to find the special types.
     * @return A list of Strings that correspond the the special types of that course.
     */
    @Operation(summary = "Get special course type by crn", description = "Retrieves if the section is honors, online, or lab based on given crn")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "400", description = "Invalid course CRN"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/course/specialCourseTypes")
    public ResponseEntity<List<String>> getSpecialCourseTypesFromCRN(
            @RequestParam(value = "crn", required = true) String crn
    ) {
        if (crn == null || crn.isEmpty()) { 
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        try {

            List<String> specialCourseTypes = courseInformationService.fetchSpecialCourseTypes(crn); // Changed method name to fetchSpecialCourseTypes

            if (specialCourseTypes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(specialCourseTypes);
            }

            return ResponseEntity.ok(specialCourseTypes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("An error occurred while fetching course types."));
        }
    }

    /**
     * Retrieves a list of all academic subjects at UGA .
     *
     * @return List of all available subjects as strings
     */
    @Operation(summary = "Get all subjects", description = "Retrieves a list of all academic subjects at UGA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subjects found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/subjects")
    @Tag(name="course-information")
    public ResponseEntity<List<String>> getAllSubjects() {

        try {

            List<String> subjects = courseInformationService.getAllSubjects();

            // Return the list of subject strings if found
            return ResponseEntity.ok(subjects);

        } catch (Exception e) {

            // Return 500 if a server error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Retrieves course information based on the given term, like Fall or Summer.
     *
     * @param term The term for which the course has to be in.
     * @return A "Course" List containing the course information from a specific term.
     */
    @Operation(summary = "Get courses by term", description = "Retrieves courses based on the given term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "400", description = "Invalid term"),
            @ApiResponse(responseCode = "404",description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/term")
    @Tag(name = "bulletin")
    public ResponseEntity<List<Course>> getCourseByTerm(@RequestParam(value = "term", required = true) String term) {

        if(term.isEmpty()){
            return ResponseEntity.badRequest().body(null); //Returns 400 if parameter isn't given
        }

        try {
            List<Course> courseList = bulletinCourseService.getCoursesByTerm(term); //fetches the courses based on term

            if(courseList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(courseList); //Checks and returns 404 error if courseList information is empty
            }

            return ResponseEntity.ok(courseList); //Returns course information if everything is correct
        } catch (Exception e) {
            //Catches any server problems or exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    /**
     * Retrieves course information based on the provided course ID.
     *
     * @param courseId The ID of the course to retrieve. (e.g., "CSCI-1301")
     * @return A "Course" List containing the course information (ID, title, description, semester).
     */
    @Operation(summary = "Get course information by course ID", description = "Retrieves course information based on the provided course ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "400", description = "Invalid course ID"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/getCourseById")
    public ResponseEntity<Course> getCourseInfo(@RequestParam(value = "courseId") String courseId) {

        // Return 400 for empty courseId
        if (courseId.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            // Call method to get course by ID
            Course course = getCourseByID(courseId);

            // Check if the course is not found (null)
            if (course == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if no course is found
            }

            // Return the course if found
            return ResponseEntity.ok(course);

        } catch (Exception e) {

            // Return 500 if a server error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves a list of courses based on the specified parameters. For example, you can edit this function to query
     * with your required parameters such as credit Hours and add on additional optional ones like,
     * "Give me all the 4 credit hours CSCI classes"
     *
     * @author Raghav Vikramprabhu
     *
     * @param creditHours The required number of credit hours for the courses. REQUIRED PARAMETER
     * @param majorCode   The optional major code (e.g., "CSCI"). OPTIONAL PARAMETER
     * @param classLevel  The optional class level (e.g., 4000). OPTIONAL PARAMETER
     * @return A list of courses that match the given criteria.
     */
    @Operation(summary = "Retrieves a list of courses based on major and class level", description = "Retrieves a list of course objects using major code 'CSCI' and/or class level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses found"),
            @ApiResponse(responseCode = "400", description = "Invalid major code"),
            @ApiResponse(responseCode = "404", description = "Courses not found")
    })
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getCourses(
            @RequestParam(value = "creditHours") int creditHours,
            @RequestParam(value = "majorCode", required = false) String majorCode,
            @RequestParam(value = "classLevel", required = false) Integer classLevel
    ) {
        // Validation for creditHours (if negative or invalid)
        if (creditHours <= 0 || creditHours > 4) {
            return ResponseEntity.badRequest().body(null); // Return 400 for bad request
        }

        try {
            // Fetch the courses based on provided filters
            List<Course> courses = bulletinCourseService.getCoursesByType(type);

            // Check if no courses are found
            if (courses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(courses); // Return 404 if no courses found
            }

            // Return the list of courses with a 200 OK response
            return ResponseEntity.ok(courses);

        } catch (Exception e) {
            // Handle any exceptions and return an internal server error response
            // You might want to log the error for debugging purposes
            // logger.error("Error fetching courses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return 500 if server error occurs
        }
    }


    /**
     * Retrieves co-requisites for a given course ID or CRN.
     *
     * @param courseId The ID of the course to retrieve co-requisites for. (optional)
     * @param crn The CRN of the course to retrieve co-requisites for. (optional)
     * @return A list of course objects that are co-requisites for the given course.
     */
    @Operation(summary = "Get coreqs by course ID", description = "Retrieves co-requisites based on the provided course ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "400", description = "Invalid course ID"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/course/coreqs")
    public ResponseEntity<List<Course>> getCoReqs(
            @RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "crn", required = false) String crn
    ) {
        // Check if both courseId and crn are null or empty
        if ((courseId == null || courseId.isEmpty()) && (crn == null || crn.isEmpty())) {
            return ResponseEntity.badRequest().body(null); // Return 400 if neither parameter is provided
        }

        try {
            // Fetch co-requisite courses using the courseId or crn
            List<Course> coReqs = bulletinCourseService.getCoReqCourses(courseId, crn); // Replace with actual method to fetch co-requisites

            // If no co-requisites found, return 404
            if (coReqs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(coReqs);
            }

            // Return the list of co-requisite courses
            return ResponseEntity.ok(coReqs);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Retrieves pre-requisites for a given course ID or CRN.
     *
     * @param courseId The ID of the course to retrieve pre-requisites for. (optional)
     * @param crn The CRN of the course to retrieve pre-requisites for. (optional)
     * @return A list of course objects that are pre-requisites for the given course.
     */
    @Operation(summary = "Get prereqs by course ID", description = "Retrieves pre-requisites based on the provided course ID or CRN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "400", description = "Invalid course ID"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/course/prereqs")
    public ResponseEntity<List<Course>> getPreReqs(
            @RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "crn", required = false) String crn
    ) {
        // Check if both courseId and crn are null or empty
        if ((courseId == null || courseId.isEmpty()) && (crn == null || crn.isEmpty())) {
            return ResponseEntity.badRequest().body(null); // Return 400 if neither parameter is provided
        }

        try {
            // Fetch pre-requisite courses using the courseId or crn
            List<Course> preReqs = fetchPreReqs(courseId, crn); // Replace with actual method to fetch pre-requisites

            // If no pre-requisites found, return 404
            if (preReqs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(preReqs);
            }

            // Return the list of pre-requisite courses
            return ResponseEntity.ok(preReqs);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Gets course sections from class time and optionally class name
     * @param timeSlot The timeslot range to use for this (10:00 AM - 11:15 AM)
     * @param crn The crn course to retrieve course sections (optional)
     * @return A list of Strings that correspond the the special types of that course.
     */
    @GetMapping("/course/sections")
    public ResponseEntity<List<Section>> getCourseSections(
            @RequestParam(value = "timeSlot", required = true) String timeSlot,
            @RequestParam(value = "crn", required = false) String crn
    ) {
        if ((timeSlot == null || timeSlot.isEmpty()) && (crn == null || crn.isEmpty())) {
            return ResponseEntity.badRequest().body(null); // Return 400 if neither parameter is provided
        }

        try {
            List<Section> courseSections = fetchCourseSection(timeSlot, crn);

            if (courseSections.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(courseSections);
            }

            return ResponseEntity.ok(courseSections);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }
    }

    // Other endpoints related to Bulletin data could be added here

    /**
     * Retrieves a list of sections that matches the requirements given.
     *
     * @param requirement The string name for a requirement
     * @return A list of courses that fufill the requirement
     */
    @Operation(summary = "get courses by requirement", description = "Retrieves a list of course objects with the given requirement fufilled")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "400", description = "Invalid requirement"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/requirement")
    @Tag(name = "bulletin")
    public ResponseEntity<List<Course>> getRequirementCourses (@RequestParam("requirement") String requirement){
        //return 400 for empty requirement
        if (requirement.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            List<Course> courses = getCoursesByRequirement(requirement);
            if (courses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                // Return 404 if no courses are found
            }
            // Return courses if found
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            // Return 500 if a server error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

} // CourseInfoController
