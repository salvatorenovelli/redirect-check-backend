import React, {Component} from 'react';
import Dropzone from 'react-dropzone'
import Request from 'superagent'

import './App.css';

class FileUploader extends Component {
    constructor() {
        super();
        this.state = {files: []}
    }

    onDrop(files) {
        this.setState({
            files
        });

        const req = Request.post('api/upload');
        files.forEach(file => {
            req.attach("file", file);
        });

        req.end(() => console.log("Upload done"));
    }

    render() {
        return (
            <section>
                <div className="dropzone-container">
                    <Dropzone onDrop={this.onDrop.bind(this)}
                              className="dropzone"
                              multiple={false}>
                        <p>Drop your excel file here, or click to select files to upload.</p>
                    </Dropzone>
                </div>
            </section>
        );
    }
}


export default FileUploader;