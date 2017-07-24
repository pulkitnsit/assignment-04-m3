package com.codenation.models;

import javax.persistence.*;

/**
 * Created by pulkit on 10/07/17.
 */

@Entity
@Table(name="contact_us")
public class ContactUs {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long feedbackId;
    private String emailId;
    private String feedbackText;

    public long getFeedbackId() {
        return feedbackId;
    }

}
