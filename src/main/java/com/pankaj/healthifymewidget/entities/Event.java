package com.pankaj.healthifymewidget.entities;

import java.io.Serializable;

/**
 * Created by Pankaj on 19/03/16.
 */
public class Event implements Serializable {

    ResponseList[] response;

    public ResponseList[] getResponse() {
        return response;
    }

    public void setResponse(ResponseList[] response) {
        this.response = response;
    }


    public class ResponseList {
        String status;
        String challenge_type;
        String start_timestamp;
        String description;
        String end_date;
        String title;
        String url;
        String is_hackerearth;
        String thumbnail;
        String end_tz;
        String end_utc_tz;
        String subscribe;
        String college;
        String end_time;
        String time;
        String date;
        String start_utc_tz;
        String start_tz;
        String cover_image;
        String end_timestamp;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getChallenge_type() {
            return challenge_type;
        }

        public void setChallenge_type(String challenge_type) {
            this.challenge_type = challenge_type;
        }

        public String getStart_timestamp() {
            return start_timestamp;
        }

        public void setStart_timestamp(String start_timestamp) {
            this.start_timestamp = start_timestamp;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIs_hackerearth() {
            return is_hackerearth;
        }

        public void setIs_hackerearth(String is_hackerearth) {
            this.is_hackerearth = is_hackerearth;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getEnd_tz() {
            return end_tz;
        }

        public void setEnd_tz(String end_tz) {
            this.end_tz = end_tz;
        }

        public String getEnd_utc_tz() {
            return end_utc_tz;
        }

        public void setEnd_utc_tz(String end_utc_tz) {
            this.end_utc_tz = end_utc_tz;
        }

        public String getSubscribe() {
            return subscribe;
        }

        public void setSubscribe(String subscribe) {
            this.subscribe = subscribe;
        }

        public String getCollege() {
            return college;
        }

        public void setCollege(String college) {
            this.college = college;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStart_utc_tz() {
            return start_utc_tz;
        }

        public void setStart_utc_tz(String start_utc_tz) {
            this.start_utc_tz = start_utc_tz;
        }

        public String getStart_tz() {
            return start_tz;
        }

        public void setStart_tz(String start_tz) {
            this.start_tz = start_tz;
        }

        public String getCover_image() {
            return cover_image;
        }

        public void setCover_image(String cover_image) {
            this.cover_image = cover_image;
        }

        public String getEnd_timestamp() {
            return end_timestamp;
        }

        public void setEnd_timestamp(String end_timestamp) {
            this.end_timestamp = end_timestamp;
        }
    }
}
