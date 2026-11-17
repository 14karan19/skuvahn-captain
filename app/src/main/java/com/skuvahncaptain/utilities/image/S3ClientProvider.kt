package com.skuvahncaptain.utilities.image

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client

/**
 * Builds an S3 client that uses short-lived credentials from a Cognito Identity Pool
 * instead of long-lived IAM keys. No AWS secret is shipped inside the APK.
 *
 * The identity pool ID is a public identifier (not a secret). Permissions are controlled
 * by the IAM role attached to the pool, which should allow only s3:PutObject on this bucket.
 */
object S3ClientProvider {

    const val BUCKET_NAME = "skuvahn-profile-images"
    private val REGION = Regions.AP_SOUTH_1

    private const val COGNITO_IDENTITY_POOL_ID = "ap-south-1:09acff62-b4de-4dec-8b85-6efdf35bab0f"

    @Volatile
    private var client: AmazonS3Client? = null

    fun get(context: Context): AmazonS3Client {
        check(!COGNITO_IDENTITY_POOL_ID.contains("REPLACE")) {
            "Cognito Identity Pool ID is not configured in S3ClientProvider"
        }
        return client ?: synchronized(this) {
            client ?: AmazonS3Client(
                CognitoCachingCredentialsProvider(
                    context.applicationContext,
                    COGNITO_IDENTITY_POOL_ID,
                    REGION
                )
            ).apply { setRegion(Region.getRegion(REGION)) }.also { client = it }
        }
    }

    fun publicUrl(key: String) = "https://$BUCKET_NAME.s3.ap-south-1.amazonaws.com/$key"
}
