import React from 'react';
import FileUploader from './FileUploader'
import Tasks from './Tasks'


export default () =>
    <div className="App">
        <div className="App-header">
            <h2>Welcome to Redirect Check!</h2>
        </div>
        <div className="container">
            <FileUploader/>
            <Tasks/>
        </div>
    </div>



