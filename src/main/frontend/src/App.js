import React, {Component} from 'react';
import Dropzone from 'react-dropzone'
import Request from 'superagent'
import './App.css';


class App extends Component {
    render() {
        return (
            <div className="App">
                <div className="App-header">
                    <h2>Welcome to Redirect Check!</h2>
                </div>
                <div className="container">
                    <FileUploader/>
                    <Tasks/>
                </div>
            </div>
        );
    }
}


class Tasks extends Component {

    constructor() {
        super();
        this.state = {
            data: []
        };
        this.tick = this.tick.bind(this);
    }

    tick() {
        Request
            .get('/api/tasks')
            .set('Accept', 'application/json')
            .end((err, res) => {
                this.setState({data: res.body});
            });
    }

    componentWillUnmount() {
        clearInterval(this.interval);
    }

    componentDidMount() {
        this.interval = setInterval(this.tick, 1000);

    }

    render() {
        return (
            <div className="highlight">
                <div className="table-responsive">
                    <table className="table text-left col-md-12">
                        <thead>
                        <tr>
                            <th className="col-sm-4 col-md-4 col-lg-4">Source File</th>
                            <th className="col-sm-4 col-md-4 col-lg-4">Status</th>
                            <th className="col-sm-4 col-md-4 col-lg-4">Output</th>
                        </tr>
                        </thead>
                        <tbody>
                        {
                            this.state.data.map((row, i) =>
                                <tr key={i}>
                                    <td>{row.inputFileName}</td>
                                    <td>
                                        <div className="progress text-center">
                                            <div className="progress-bar progress-bar-info progress-bar-striped"
                                                 role="progressbar" aria-valuenow="60"
                                                 aria-valuemin="0" aria-valuemax="100"
                                                 style={{width: row.taskProgress.percentageCompleted + '%'}}>
                                                {row.status}
                                            </div>
                                        </div>
                                    </td>
                                    <td><a href={'/api/files/' + row.outputUri}>{row.outputUri}</a></td>
                                </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

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

export default App;
