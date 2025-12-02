import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { getActivityRecommendation } from '../services/api';
import { Box, Card, CardContent, Divider, Typography } from '@mui/material';

const ActivityDetail = () => {
    const {id} = useParams();
    const [activity, setActivity] = useState(null);
    const [recommendation, setRecommendation] = useState(null);

    useEffect(() => {
        const fetchActivityRecommendations = async () => {
            try{
                const response = await getActivityRecommendation(id);
                setActivity(response.data);
                setRecommendation(response.data.recommendation);
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

            {activity?.recommendation && (
                <Card>
                    <CardContent>
                        <Typography variant='h5' gutterBottom>AI Recommendation</Typography>

                        <Typography variant='h6'>Analisys</Typography>
                        <Typography paragraph>{activity.recommendation}</Typography>

                        <Divider sx={{my:2}}/>

                        <Typography variant='h6'>Improvements</Typography>
                        {Array.isArray(activity.improvements) && activity.improvements.length > 0 ?(
                            activity.improvements.map((improvements, index) => (
                                <Typography key={index} paragraph>{improvements}</Typography>
                            ))
                        ) : (
                            <Typography paragraph>No improvements suggested.</Typography>
                        )}

                        <Divider sx={{my:2}}/>

                        <Typography variant='h6'>suggestions</Typography>
                        {Array.isArray(activity.suggestions) && activity.suggestions.length > 0 ?(
                            activity.suggestions.map((suggestion, index) => (
                                <Typography key={index} paragraph>{suggestion}</Typography>
                            ))
                        ) : (
                            <Typography paragraph>No suggestions.</Typography>
                        )}

                        <Divider sx={{my:2}}/>

                        <Typography variant='h6'>Safety Guidelines</Typography>
                        {Array.isArray(activity.safety) && activity.safety.length > 0 ?(
                            activity.safety.map((safety, index) => (
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