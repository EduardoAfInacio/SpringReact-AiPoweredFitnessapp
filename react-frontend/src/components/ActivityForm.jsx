import { TextField } from '@mui/material';
import {React, useState} from 'react';
import { Button, Box, FormControl, InputLabel, Select, MenuItem } from "@mui/material";
import { saveActivity } from '../services/api';


const ActivityForm = ({onActivityAdded}) => {

    const [activity, setActivity] = useState({
        activityType: "RUNNING",
        duration: '',
        calories: '',
        additionalMetrics: {}
    })

    const handleSubmit = async (e) => {
        e.preventDefault();
        try{
            saveActivity(activity);
            onActivityAdded();
            setActivity({type: "RUNNING", duration: 0, caloriesBurned: ''});
        }catch(error){
            console.error("Error submitting activity:", error);
        }
    }

    return (
        <Box component='form' onSubmit={handleSubmit} sx={{ mb: 4 }}>
            <FormControl fullWidth sx={{ mb: 2}}>
                <InputLabel>Activity Type</InputLabel>
                <Select
                value={activity.type}
                onChange={(e) => setActivity({ ...activity, activityType: e.target.value })}
                >
                    <MenuItem value="RUNNING">Running</MenuItem>
                    <MenuItem value="CYCLING">Cycling</MenuItem>
                    <MenuItem value="SWIMMING">Swimming</MenuItem>
                    <MenuItem value="WALKING">Walking</MenuItem>
                    <MenuItem value="WEIGHTLIFTING">Weightlifting</MenuItem>
                    <MenuItem value="YOGA">Yoga</MenuItem>
                    <MenuItem value="CARDIO">Cardio</MenuItem>
                    <MenuItem value="STRETCHING">Stretching</MenuItem>
                    <MenuItem value="PULLUP">Pull-Up</MenuItem>
                    <MenuItem value="SQUAT">Squat</MenuItem>
                    <MenuItem value="BENCH_PRESS">Bench Press</MenuItem>
                    <MenuItem value="LEG_RAISE">Leg Raise</MenuItem>
                    <MenuItem value="LUNGE">Lunge</MenuItem>
                    <MenuItem value="BARBELL_SHOULDER_PRESS">Barbell Shoulder Press</MenuItem>
                    <MenuItem value="LEG_CURL">Leg Curl</MenuItem>
                    <MenuItem value="HIP_THRUST">Hip Thrust</MenuItem>
                    <MenuItem value="HIP_EXTENSION">Hip Extension</MenuItem>
                </Select>
            </FormControl>
            <TextField
                fullWidth
                label="Duration (minutes)"
                type="number"
                sx={{mb:2}}
                value={activity.duration}
                onChange={(e) => setActivity({...activity, duration: e.target.value})}/>
            <TextField
                fullWidth
                label="Calories Burned"
                type="number"
                sx={{mb:2}}
                value={activity.caloriesBurned}
                onChange={(e) => setActivity({...activity, calories: e.target.value})}/>
            <Button type="submit" variant="contained">
                Add Activity
            </Button>
        </Box>
    )
}

export default ActivityForm;