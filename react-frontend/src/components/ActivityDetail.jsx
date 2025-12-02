import React, { useEffect, useState } from 'react';
import { useLocation, useParams } from 'react-router';
import { getActivityRecommendation } from '../services/api';
import { Box, Card, CardContent, Divider, Typography } from '@mui/material';

const ActivityDetail = () => {
    const {id} = useParams();
    const {activity} = useLocation().state || {};
    const [recommendation, setRecommendation] = useState(null);

    useEffect(() => {
        const fetchActivityRecommendations = async () => {
            try{
                const response = await getActivityRecommendation(id);
                setRecommendation(response.data);
            }catch(error){
                console.error("Error fetching activity details:", error);
            }
        }

        fetchActivityRecommendations();
    }, [id]);

    if(!activity){
        return <Typography>Loading...</Typography>
    }

    return (
        <Box sx={{ maxWidth: 800, mx: 'auto', p: 2, border: '1px solid grey', borderRadius: 2 }}>
            <Card sx={{mb:2}}>
                <CardContent>
                    <Typography variant='h5' gutterBottom>Activity Details</Typography>
                    <Typography>Type: {activity.type}</Typography>
                    <Typography>Duration: {activity.duration} minutes</Typography>
                    <Typography>Calories Burned: {activity.caloriesBurned}</Typography>
                    <Typography>Date: {new Date(activity.createdAt).toLocaleString("pt-BR")}</Typography>
                </CardContent>
            </Card>

            {recommendation?.recommendation && (
                <Card>
                    <CardContent>
                        <Typography variant='h5' gutterBottom>AI Recommendation</Typography>

                        <Typography variant='h6'>Analisys</Typography>
                        <Typography paragraph>{recommendation.recommendation}</Typography>

                        <Divider sx={{my:2}}/>

                        <Typography variant='h6'>Improvements</Typography>
                        {Array.isArray(recommendation.improvements) && recommendation.improvements.length > 0 ?(
                            recommendation.improvements.map((improvements, index) => (
                                <Typography key={index} paragraph>{improvements}</Typography>
                            ))
                        ) : (
                            <Typography paragraph>No improvements suggested.</Typography>
                        )}

                        <Divider sx={{my:2}}/>

                        <Typography variant='h6'>suggestions</Typography>
                        {Array.isArray(recommendation.suggestions) && recommendation.suggestions.length > 0 ?(
                            recommendation.suggestions.map((suggestion, index) => (
                                <Typography key={index} paragraph>{suggestion}</Typography>
                            ))
                        ) : (
                            <Typography paragraph>No suggestions.</Typography>
                        )}

                        <Divider sx={{my:2}}/>

                        <Typography variant='h6'>Safety Guidelines</Typography>
                        {Array.isArray(recommendation.safety) && recommendation.safety.length > 0 ?(
                            recommendation.safety.map((safety, index) => (
                                <Typography key={index} paragraph>{safety}</Typography>
                            ))
                        ) : (
                            <Typography paragraph>No safety guidelines.</Typography>
                        )}

                    </CardContent>
                </Card>
            )}
        </Box>
    )
}

export default ActivityDetail;