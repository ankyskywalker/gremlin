package com.tinkerpop.gremlin.compiler.functions.g.graph;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.parser.GraphMLReader;
import com.tinkerpop.gremlin.compiler.Atom;
import com.tinkerpop.gremlin.compiler.functions.AbstractFunction;
import com.tinkerpop.gremlin.compiler.functions.helpers.GraphFunctionHelper;
import com.tinkerpop.gremlin.compiler.operations.Operation;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author Pavel A. Yaskevich
 */
public class LoadFunction extends AbstractFunction {

    private static final String FUNCTION_NAME = "load";


    public Atom compute(List<Operation> params) throws RuntimeException {
        if (params.size() == 0 || params.size() > 2)
            throwUnsupportedArguments();

        Graph graph = null;
        String filename = null;

        // graph variable as first param
        if (params.size() == 2) {
            graph = GraphFunctionHelper.getGraph(params.get(0));
            filename = (String) params.get(1).compute().getValue();
        } else {
            // only filename in params
            graph = GraphFunctionHelper.getGraph(null);
            filename = (String) params.get(0).compute().getValue();
        }

        try {
            InputStream stream;
            try {
                stream = new URL(filename).openStream();
            } catch (MalformedURLException urlEx) {
                stream = new FileInputStream(filename);
            }

            GraphMLReader.inputGraph(graph, stream);
            return new Atom(true);
        } catch (Exception e) {
            throw new RuntimeException("g:load - " + e.getMessage());
        }
    }

    public String getFunctionName() {
        return FUNCTION_NAME;
    }


}