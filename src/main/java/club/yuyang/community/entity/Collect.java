package club.yuyang.community.entity;

public class Collect {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column collect.id
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column collect.question_id
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    private Long questionId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column collect.user_id
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column collect.gmt_create
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    private Long gmtCreate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column collect.id
     *
     * @return the value of collect.id
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column collect.id
     *
     * @param id the value for collect.id
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column collect.question_id
     *
     * @return the value of collect.question_id
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    public Long getQuestionId() {
        return questionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column collect.question_id
     *
     * @param questionId the value for collect.question_id
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column collect.user_id
     *
     * @return the value of collect.user_id
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column collect.user_id
     *
     * @param userId the value for collect.user_id
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column collect.gmt_create
     *
     * @return the value of collect.gmt_create
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column collect.gmt_create
     *
     * @param gmtCreate the value for collect.gmt_create
     *
     * @mbggenerated Tue Aug 20 17:35:20 CST 2019
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}