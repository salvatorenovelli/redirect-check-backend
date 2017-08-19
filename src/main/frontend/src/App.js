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
                <Basic/>

                <Tasks/>


                <p className="App-intro">
                    To get started, edit <code>src/App.js</code> and save to reload.
                </p>
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
            <div className="jumbotron">
                <table className="table">
                    <thead>
                    <tr>
                        <th>Source File</th>
                        <th>Status</th>
                        <th>Output</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.data.map((row, i) =>
                            <tr key={i}>
                                <td>{row.inputFileName}</td>
                                <td>{row.status + ': ' + row.taskProgress.percentageCompleted + '%'}</td>
                                <td><a href={'/api/files/' + row.outputUri}>{row.outputUri}</a></td>
                            </tr>
                        )
                    }
                    </tbody>
                </table>
            </div>
        );
    }
}

class Basic extends Component {
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

        req.end(() => console.log("done"));
    }

    render() {
        return (
            <section>
                <div className="dropzone">
                    <Dropzone onDrop={this.onDrop.bind(this)}>
                        <p>Try dropping some files here, or click to select files to upload.</p>
                    </Dropzone>
                </div>
                <aside>
                    <h2>Dropped files</h2>
                    <ul>
                        {
                            this.state.files.map(f => <li>{f.name} - {f.size} bytes</li>)
                        }
                    </ul>
                </aside>
            </section>
        );
    }
}

export default App;
