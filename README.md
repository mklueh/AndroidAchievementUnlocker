# acu

ACU - Android Content Unlocker

This small library allows you to solve complex unlocking tasks.

Example:

- You have two locked Resources (Images, Features, etc...) A, B and C

- You have three Reward Triggers
    1. User rates the app
    2. User shares the app
    3. User keeps the app for at least 2 days


With ACU, you can define multiple unlock relations between triggers and resources

Examples:

    1. Unlock A, if user rates the app
    2. Unlock B, if user shares the app
    3. Unlock C, if user rates the app