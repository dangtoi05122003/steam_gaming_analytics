# steam_gaming_analytics

> **Understanding user sentiment and behavior across large-scale digital platforms is essential for product evolution. Driven by a passion for analytics engineering, I built this project to transform millions of raw Steam reviews into structured analytical models, bridging the gap between player feedback and actionable insights.**

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Data Quality](#data-quality)
- [Dashboard](#dashboard)
- [Data Source](#data-source)
- [Project Structure](#project-structure)

---

## Overview

&nbsp;&nbsp;&nbsp;&nbsp;Designed to handle high-volume user feedback datasets, this project establishes a modern analytics pipeline for sentiment and market analysis. Raw tabular review records are cleaned, modeled, and transformed into structured analytical marts, enabling granular exploration into review volume, long-term sentiment trends, language distribution, and game popularity.

---

## Architecture

&nbsp;&nbsp;&nbsp;&nbsp;ForWorkflows are orchestrated sequentially through distributed stages. Ingestion jobs process and store raw information into storage and metastores to establish foundational data lakes. Subsequently, transformation tools process and materialize these datasets within query and warehousing environments to deliver optimized analytical marts directly to visualization layers.

![Architecture](images/architecture.png)

&nbsp;&nbsp;&nbsp;&nbsp;For the analytical data model, the architecture relies on a Star Schema designed around a central fact table. This primary fact entity links cleanly to its surrounding dimensional tables to eliminate data redundancy, streamline relationships, and power multi-dimensional dashboards efficiently.

![Star Schema](images/star_schema.png)

## Data Quality

### 1.Completeness

| Column | NULL Count | Status |
| ------ | ---------- | ------ |
| recommendationid | 0 | PASS |
| appid | 0 | PASS |
| game | 5,201 | RESOLVED |
| author_steamid | 0 | PASS |
| author_num_games_owned | 0 | PASS |
| author_num_reviews | 0 | PASS |
| author_playtime_forever | 8 | PASS |
| author_playtime_last_two_weeks | 8 | PASS |
| author_playtime_at_review | 0 | PASS |
| author_last_played | 8 | PASS |
| language | 0 | PASS |
| review | 0 | PASS |
| timestamp_created | 0 | PASS |
| timestamp_updated | 0 | PASS |
| voted_up | 0 | PASS |
| votes_up | 0 | PASS |
| votes_funny | 1912 | PASS |
| weighted_vote_score | 0 | PASS |
| comment_count | 1 | PASS |
| steam_purchase | 0 | PASS |
| received_for_free | 0 | PASS |
| written_during_early_access | 0 | PASS |
| hidden_in_steam_china | 0 | PASS |
| steam_china_location | 113,882,695 | PASS |

### 2.Timeliness

| Check | Condition | Result | Status |
| ----- | --------- | ------ | ------ |
| Created timestamp validity | `timestamp_created > current_timestamp` | 0 | PASS |
| Updated timestamp validity | `timestamp_updated > current_timestamp` | 0 | PASS |
| Last played timestamp validity | `author_last_played > current_timestamp` | 0 | PASS |
| Timestamp sequence | `timestamp_updated < timestamp_created` | 2 | RESOLVED |

### 3.Validity

| Check | Condition | Result | Status |
| ----- | --------- | ------ | ------ |
| Recommendation ID validity | `recommendationid <= 0` | 0 | PASS |
| App ID validity | `appid <= 0` | 0 | PASS |
| Author Steam ID validity | `author_steamid <= 0` | 0 | PASS |
| Games owned validity | `author_num_games_owned < 0` | 0 | PASS |
| Reviews count validity | `author_num_reviews < 0` | 0 | PASS |
| Playtime forever validity | `author_playtime_forever < 0` | 0 | PASS |
| Playtime last two weeks validity | `author_playtime_last_two_weeks < 0` | 0 | PASS |
| Playtime at review validity | `author_playtime_at_review < 0` | 0 | PASS |
| Votes up validity | `votes_up < 0` | 0 | PASS |
| Votes funny validity | `votes_funny < 0` | 0 | PASS |
| Weighted vote score validity | `weighted_vote_score < 0 OR weighted_vote_score > 1` | 0 | PASS |
| Comment count validity | `comment_count < 0` | 0 | PASS |

### 4.Integrity

| Check | Columns | Condition | Result | Status |
| ----- | ------- | --------- | ------ | ------ |
| Playtime Logic | author_playtime_at_review, author_playtime_forever | `author_playtime_at_review > author_playtime_forever` | 28572911 | RESOLVED |
| Foreign Key Validity | appid | appid is not present in the referenced data | 0 | PASS |

### 5.Uniqueness

| Column | Check Type| Result | Status |
| ------ | --------- | -------| ------ |
| recommendationid, appid | Duplicate | 0 | PASS |

### 6.Consistency

| Check | Description | Result | Status |
| ----- | ----------- | ------ | ------ |
| Boolean Flag Logic | steam_purchase and received_for_free contain no conflicting values | 1350036 | RESOLVED |

---

## Dashboard

### 1. Overview Dashboard

&nbsp;&nbsp;&nbsp;&nbsp;Provides a high-level overview by consolidating key performance indicators, tracking monthly review volume and positive review rate over time, and breaking down engagement distribution across international user languages.

![Overview Dashboard](images/overview.png)

### 2. Product Deep-Dive

&nbsp;&nbsp;&nbsp;&nbsp;Focuses on item-level performance by evaluating review volume against approval percentages, tracking long-term daily review trends, comparing Steam-purchased reviews with reviews received for free, and offering granular metrics for cross-product benchmarking.

![Product Deep-Dive](images/game_deep_dive.png)

### 3. Market & Localization

&nbsp;&nbsp;&nbsp;&nbsp;Evaluates user sentiment consistency across localized language markets while quantifying total hidden review volumes and measuring visibility share within Steam China.

![Market & Localization](images/market_localization.png)

---

## Data Source

&nbsp;&nbsp;&nbsp;&nbsp;Primary data is sourced from the [Steam Reviews Dataset](https://www.kaggle.com/datasets/kieranpoc/steam-reviews) on Kaggle. This comprehensive repository serves as the baseline for the entire pipeline, offering millions of granular historical records that capture player feedback, playtimes, sentiment ratings, purchase status, and regional localization attributes across diverse global markets.

---

## Project Structure

```
steam_gaming_analytics/
├── steam_analytics
│   └── models
│       ├── dim
│       ├── fact
│       └── staging
│
├── config
│
├── dags
│   ├── configs
│   │   ├── bronze
│   │   └── silver
│   └── root
│
├── src/main/java/com/steam/
│   ├── bronze
│   ├── silver
│   ├── config
│   └── trino
│
├── plugins
│   ├── generators
│   └── models
│
├── .env
├── .gitignore
├── pom.xml
└── README.md
```